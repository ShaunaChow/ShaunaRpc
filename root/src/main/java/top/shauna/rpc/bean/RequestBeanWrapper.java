package top.shauna.rpc.bean;

import java.io.Serializable;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class RequestBeanWrapper implements Serializable {
    private String uuid;
    private Long id;
    private RequestBean requestBean;

    public RequestBeanWrapper() {
    }

    public RequestBeanWrapper(long id, RequestBean referenceBean) {
        this.id = id;
        this.requestBean = referenceBean;
    }

    public String getUuid() {
        return uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public RequestBean getRequestBean() {
        return requestBean;
    }

    public void setRequestBean(RequestBean referenceBean) {
        this.requestBean = referenceBean;
    }
}
