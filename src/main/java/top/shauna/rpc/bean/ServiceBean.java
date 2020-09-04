package top.shauna.rpc.bean;

import top.shauna.rpc.config.PubConfig;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

public class ServiceBean<T> {
    private URL url;
    private T interfaceImpl;
    private Map<String,Method> methods;
    private PubConfig pubConfig;

    public ServiceBean() {
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public T getInterfaceImpl() {
        return interfaceImpl;
    }

    public void setInterfaceImpl(T interfaceImpl) {
        this.interfaceImpl = interfaceImpl;
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public void setMethods(Map<String, Method> methods) {
        this.methods = methods;
    }

    public PubConfig getPubConfig() {
        return pubConfig;
    }

    public void setPubConfig(PubConfig pubConfig) {
        this.pubConfig = pubConfig;
    }
}
