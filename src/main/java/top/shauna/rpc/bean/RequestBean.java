package top.shauna.rpc.bean;

import java.util.List;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class RequestBean {

    private String clazzName;
    private String method;
    private List<String> values;

    public RequestBean() {
    }

    public RequestBean(String clazzName, String method, List<String> values) {

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

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
