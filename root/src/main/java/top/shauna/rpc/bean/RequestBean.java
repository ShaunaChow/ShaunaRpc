package top.shauna.rpc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class RequestBean implements Serializable {

    private String clazzName;
    private String method;
    private List<Object> values;

    public RequestBean() {
    }

    public RequestBean(String clazzName, String method, List<Object> values) {

        this.clazzName = clazzName;
        this.method = method;
        this.values = values;
    }

    public String getClazzName() {

        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
