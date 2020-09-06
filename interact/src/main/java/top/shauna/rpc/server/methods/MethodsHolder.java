package top.shauna.rpc.server.methods;

import top.shauna.rpc.bean.ServiceBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodsHolder {

    private static Map<String,ServiceBean> methods = new ConcurrentHashMap<>();

    public static ServiceBean getServiceBean(String className){
        return methods.get(className);
    }

    public static void putMethod(String className, ServiceBean serviceBean){
        methods.put(className,serviceBean);
    }
}
