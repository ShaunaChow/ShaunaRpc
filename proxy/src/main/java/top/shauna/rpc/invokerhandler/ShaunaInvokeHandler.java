package top.shauna.rpc.invokerhandler;

import com.alibaba.fastjson.JSON;
import top.shauna.rpc.bean.ReferenceBean;
import top.shauna.rpc.bean.RemoteClient;
import top.shauna.rpc.bean.RequestBean;
import top.shauna.rpc.interfaces.Channel;
import top.shauna.rpc.interfaces.LoadBalance;
import top.shauna.rpc.loadbalance.ShaunaPollBalance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
        RemoteClient client = loadBalancer.getRemoteClient(referenceBean);
        Channel channel = client.getChannel();
        List<String> values = new ArrayList<>();
        for (Object arg : args) {
            values.add(JSON.toJSONString(arg));
        }
        RequestBean requestBean = new RequestBean(referenceBean.getClassName(),method.getName(), values);
        channel.write(JSON.toJSONString(requestBean));
        return null;
    }
}
