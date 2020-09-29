package top.shauna.rpc.bean;

import java.io.Serializable;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class ResponseBeanWrapper implements Serializable {
    private String uuid;
    private Long id;
    private ResponseBean responseBean;

    public ResponseBeanWrapper() {
    }

    public ResponseBeanWrapper(Long id, ResponseBean responseBean) {
        this.id = id;
        this.responseBean = responseBean;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {

        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ResponseBean getResponseBean() {
        return responseBean;
    }

    public void setResponseBean(ResponseBean responseBean) {
        this.responseBean = responseBean;
    }
}
