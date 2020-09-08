package top.shauna.rpc.bean;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FoundBean {
    private String potocol;
    private String url;
    private String loc;

    public FoundBean(String potocol, String url, String loc) {
        this.potocol = potocol;
        this.url = url;
        this.loc = loc;
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

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
