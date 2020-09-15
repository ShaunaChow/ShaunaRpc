package top.shauna.rpc.bean;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class LocalExportBean {

    private String protocol;
    private int port;
    private String ip;
    private String serverClassLoc;
    private String clientClassLoc;
    private Boolean isExported;

    public LocalExportBean() {
    }

    public LocalExportBean(String protocol, int port, String ip) {
        this.protocol = protocol;
        this.port = port;
        this.ip = ip;
    }

    public String getServerClassLoc() {
        return serverClassLoc;
    }

    public void setServerClassLoc(String serverClassLoc) {
        this.serverClassLoc = serverClassLoc;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getClientClassLoc() {
        return clientClassLoc;
    }

    public void setClientClassLoc(String clientClassLoc) {
        this.clientClassLoc = clientClassLoc;
    }

    public Boolean getExported() {
        return isExported;
    }

    public void setExported(Boolean exported) {
        isExported = exported;
    }

    @Override
    public String toString() {
        return "LocalExportBean{" +
                "protocol='" + protocol + '\'' +
                ", port=" + port +
                ", ip='" + ip + '\'' +
                ", serverClassLoc='" + serverClassLoc + '\'' +
                ", clientClassLoc='" + clientClassLoc + '\'' +
                '}';
    }
}
