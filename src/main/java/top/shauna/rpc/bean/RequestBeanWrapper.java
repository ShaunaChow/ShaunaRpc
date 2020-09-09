package top.shauna.rpc.bean;

public class RequestBeanWrapper {
    private String uuid;
    private RequestBean requestBean;

    public RequestBeanWrapper() {
    }

    public RequestBeanWrapper(String uuid, RequestBean referenceBean) {
        this.uuid = uuid;
        this.requestBean = referenceBean;
    }

    public String getUuid() {
        return uuid;
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
