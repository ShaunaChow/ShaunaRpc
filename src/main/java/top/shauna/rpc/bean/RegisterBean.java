package top.shauna.rpc.bean;

public class RegisterBean {
    private String potocol;
    private String url;
    private String loc;

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public RegisterBean() {
    }

    public String getPotocol() {
        return potocol;
    }

    public void setPotocol(String potocol) {
        this.potocol = potocol;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
