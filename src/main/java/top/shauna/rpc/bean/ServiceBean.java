package top.shauna.rpc.bean;

import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

@NoArgsConstructor
public class ServiceBean<T> {
    private Class interfaze;
    private T interfaceImpl;
    private Map<String,Method> methods;
    private LocalExportBean localExportBean;

    public Class getInterfaze() {
        return interfaze;
    }

    public void setInterfaze(Class interfaze) {
        this.interfaze = interfaze;
    }

    public LocalExportBean getLocalExportBean() {
        return localExportBean;
    }

    public void setLocalExportBean(LocalExportBean localExportBean) {
        this.localExportBean = localExportBean;
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
}
