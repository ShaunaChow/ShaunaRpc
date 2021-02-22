package top.shauna.rpc.invokerhandler;

import lombok.extern.slf4j.Slf4j;
import top.shauna.rpc.bean.*;
import top.shauna.rpc.client.responseholder.NettyMessageHolder;
import top.shauna.rpc.config.PubConfig;
import top.shauna.rpc.interfaces.Channel;
import top.shauna.rpc.interfaces.LoadBalance;
import top.shauna.rpc.loadbalance.ShaunaPollBalance;
import top.shauna.rpc.type.ResponseEnum;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

@Slf4j
public class ShaunaInvokeHandler implements InvocationHandler {
    private ReferenceBean referenceBean;
    private LoadBalance loadBalancer;

    public ShaunaInvokeHandler(ReferenceBean referenceBean){
        this.referenceBean = referenceBean;
        String loadbalance = PubConfig.getInstance().getLoadbalance();
        try {
            this.loadBalancer = (LoadBalance) Class.forName(loadbalance).newInstance();
        } catch (Exception e) {
            this.loadBalancer = new ShaunaPollBalance();
        }
    }

    public ShaunaInvokeHandler(ReferenceBean referenceBean, LoadBalance loadBalancer) {
        this.referenceBean = referenceBean;
        this.loadBalancer = loadBalancer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object filter = methodFilter(method);
        if(filter!=null) return filter;

        MessageBean messageBean = getResponse(method, args);
        if(messageBean==null){
            throw new Exception(method.getName()+"远端服务器未准备好!!!");
        }
        int count = 1;
        while(messageBean.getMsg()==null) {
            messageBean = getResponse(method,args);
            count++;
            /** 启动两倍容错！ **/
            if(messageBean==null||count>referenceBean.getRemoteClients().size()*2){
                throw new Exception(method.getName()+"远端服务器未准备好!!!");
            }
        }

        ResponseBean msg = (ResponseBean) messageBean.getMsg();
        switch (msg.getCode()){
            case SUCCESS:
                return msg.getRes();
            case PARAM_ERROR:
                throw new Exception(msg.getRes().toString());
            case NO_SUCH_CLASS:
                throw new Exception("远端未找到class");
            case MISSING_PARAMS:
                throw new Exception("参数错误");
            case NO_SUCH_METHOD:
                throw new Exception("远端未找到该方法："+method.getName());
            default:
                throw new Exception("未知错误");
        }
    }

    /** 对一般方法的过滤  比如toString **/
    private Object methodFilter(Method method) {
        if(method.getName().equals("toString")) return referenceBean.toString();
        return null;
    }

    private MessageBean getResponse(Method method, Object[] args){
        InvokeInfo invokeInfo = new InvokeInfo(getUniqueId(), Arrays.asList(args==null?new Object[]{}:args));
        RemoteClient client = loadBalancer.getRemoteClient(referenceBean.getRemoteClients(),invokeInfo);
        if(client==null) {
            return null;
        }
        Channel channel = client.getChannel();
        if(!channel.isOk()){
            return new MessageBean();
        }
        List<Object> values = new ArrayList<>();
        if(args!=null) {
            for (Object arg : args) {
                values.add(arg);
            }
        }
        RequestBean requestBean = new RequestBean(referenceBean.getClassName(),getMethodFullName(method), values);
        long uniqueId = getUniqueId();
        RequestBeanWrapper requestBeanWrapper = new RequestBeanWrapper(uniqueId, requestBean);

        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        MessageBean sendMessage = new MessageBean(lock, condition, null);
        NettyMessageHolder.put(uniqueId, sendMessage);
        /**启动远端调用
         * 在此插入监视器代码或其他！！！
         * **/
        long startTime = System.currentTimeMillis();
        try{
            lock.lock();
            channel.write(requestBeanWrapper);
            Long timeout = PubConfig.getInstance().getTimeout();
            if(timeout==null||timeout<1000) timeout = 1000L;
            boolean await = condition.await(timeout, TimeUnit.MILLISECONDS);
            if (!await) log.warn("调用超时");
        }catch (Exception e){
            log.error("远端调用失败！！！！"+e.getMessage());
            return sendMessage;
        }finally{
            lock.unlock();
            long endTime = System.currentTimeMillis();
            client.updateAvgTime(endTime-startTime);
        }

        MessageBean messageBean = NettyMessageHolder.getMessage(uniqueId);
        NettyMessageHolder.remote(uniqueId);

        return messageBean;
    }

    private String getMethodFullName(Method method) {
        StringBuilder sb = new StringBuilder(method.getReturnType().getName()+" "+method.getName()+"(");
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if(i==method.getParameters().length-1){
                sb.append(parameter.getType().getName()+")");
            }else sb.append(parameter.getType().getName()+" ,");
        }
        return sb.toString();
    }

    private long getUniqueId(){
        long id = System.nanoTime();
        int hashCode = Math.abs(UUID.randomUUID().hashCode());
        id = id|(hashCode<<31);
        return id;
    }
}
