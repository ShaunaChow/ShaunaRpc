package top.shauna.rpc.proxy;

import top.shauna.rpc.bean.ReferenceBean;
import top.shauna.rpc.invokerhandler.ShaunaInvokeHandler;

import java.lang.reflect.Proxy;

public class ReferenceProxyFactory {

    public static <T> T getProxy(ReferenceBean referenceBean){
        return (T) Proxy.newProxyInstance(
                ReferenceProxyFactory.class.getClassLoader(),
                new Class[]{referenceBean.getInterfaze()},new ShaunaInvokeHandler(referenceBean));
    }
}
