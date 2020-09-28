package top.shauna.rpc.invokerhandler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import top.shauna.rpc.bean.*;
import top.shauna.rpc.client.responseholder.NettyMessageHolder;
import top.shauna.rpc.config.PubConfig;
import top.shauna.rpc.interfaces.Channel;
import top.shauna.rpc.interfaces.LoadBalance;
import top.shauna.rpc.loadbalance.ShaunaPollBalance;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
        this.loadBalancer = new ShaunaPollBalance();
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
                return parseObj(msg.getRes(),method);
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

    private Object parseObj(Object res, Method method) throws Exception {
        if(res instanceof byte[]) return res;
        else if(res instanceof String) return JSON.parseObject(res.toString(),method.getGenericReturnType());
        else throw new Exception("不支持的类型");
    }

    /** 对一般方法的过滤  比如toString **/
    private Object methodFilter(Method method) {
        if(method.getName().equals("toString")) return referenceBean.toString();
        return null;
    }

    private MessageBean getResponse(Method method, Object[] args){
        RemoteClient client = loadBalancer.getRemoteClient(referenceBean.getRemoteClients());
        if(client==null) {
            return null;
        }
        Channel channel = client.getChannel();
        List<String> values = new ArrayList<>();
        if(args!=null) {
            for (Object arg : args) {
                values.add(JSON.toJSONString(arg));
            }
        }
        RequestBean requestBean = new RequestBean(referenceBean.getClassName(),getMethodFullName(method), values);
        String uuid = UUID.randomUUID().toString();
        RequestBeanWrapper requestBeanWrapper = new RequestBeanWrapper(uuid, requestBean);

        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        MessageBean sendMessage = new MessageBean(lock, condition, null);
        NettyMessageHolder.put(uuid, sendMessage);
        /**启动远端调用
         * 在此插入监视器代码或其他！！！
         * **/
        try{
            lock.lock();
            channel.write(JSON.toJSONString(requestBeanWrapper));
            Long timeout = PubConfig.getInstance().getTimeout();
            if(timeout==null||timeout<1000) timeout = 1000L;
            condition.await(timeout, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            log.error("远端调用失败！！！！"+e.getMessage());
            return sendMessage;
        }finally{
            lock.unlock();
        }

        MessageBean messageBean = NettyMessageHolder.getMessage(uuid);
        NettyMessageHolder.remote(uuid);

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
}
