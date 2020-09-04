package top.shauna.rpc.bean;

public class LocalExportBean {

    private String protocal;
    private int port;
    private String ip;

    public LocalExportBean() {
    }

    public LocalExportBean(String protocal, int port, String ip) {
        this.protocal = protocal;
        this.port = port;
        this.ip = ip;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
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
}
