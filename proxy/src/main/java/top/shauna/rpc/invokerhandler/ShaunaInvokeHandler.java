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
        MessageBean messageBean = getResponse(method, args);

        if(messageBean==null||messageBean.getMsg()==null) {
            /** 远端调用失败了!!!
             * 容错程序在此插入
             * **/
            throw new Exception("远端调用失败!!!");
        }
        ResponseBean msg = (ResponseBean) messageBean.getMsg();
        switch (msg.getCode()){
            case SUCCESS:
                return JSON.parseObject(msg.getRes().toString(),method.getReturnType());
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

    private MessageBean getResponse(Method method, Object[] args) throws Exception {
        RemoteClient client = loadBalancer.getRemoteClient(referenceBean.getRemoteClients());
        if(client==null) {
            log.info("远端服务器未准备好!!!");
            return null;
        }
        Channel channel = client.getChannel();
        List<String> values = new ArrayList<>();
        for (Object arg : args) {
            values.add(JSON.toJSONString(arg));
        }
        RequestBean requestBean = new RequestBean(referenceBean.getClassName(),method.getName(), values);
        String uuid = UUID.randomUUID().toString();
        RequestBeanWrapper requestBeanWrapper = new RequestBeanWrapper(uuid, requestBean);

        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        NettyMessageHolder.put(uuid,new MessageBean(lock,condition,null));
        /**启动远端调用
         * 在此插入监视器代码或其他！！！
         * **/
        channel.write(JSON.toJSONString(requestBeanWrapper));

        lock.lock();
        try {
            condition.await(PubConfig.getInstance().getTimeout(), TimeUnit.MILLISECONDS);
        }finally{
            lock.unlock();
        }

        MessageBean messageBean = NettyMessageHolder.getMessage(uuid);
        NettyMessageHolder.remote(uuid);

        return messageBean;
    }
}
