package top.shauna.rpc.bean;

import lombok.NoArgsConstructor;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

@NoArgsConstructor
public class ServiceBean<T> {
    private Class interfaze;
    private String ref;
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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "ServiceBean{" +
                "interfaze=" + interfaze +
                ", ref='" + ref + '\'' +
                ", interfaceImpl=" + interfaceImpl +
                ", methods=" + methods +
                ", localExportBean=" + localExportBean +
                '}';
    }
}
