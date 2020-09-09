package top.shauna.rpc.bean;

public class ResponseBeanWrapper {
    private String uuid;
    private ResponseBean responseBean;

    public ResponseBeanWrapper() {
    }

    public ResponseBeanWrapper(String uuid, ResponseBean responseBean) {

        this.uuid = uuid;
        this.responseBean = responseBean;
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
