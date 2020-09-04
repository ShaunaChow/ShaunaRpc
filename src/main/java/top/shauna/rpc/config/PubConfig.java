package top.shauna.rpc.config;

import top.shauna.rpc.bean.RegisterBean;

public class PubConfig {

    private PubConfig(){}

    private volatile PubConfig pubConfig;

    public PubConfig getInstance(){
        if(pubConfig==null){
            synchronized (PubConfig.class){
                if(pubConfig==null){
                    pubConfig = new PubConfig();
                }
            }
        }
        return pubConfig;
    }

    private String applicationName;
    private RegisterBean registerBean;
    private String ipaddr;

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    private String exportPort;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getExportPort() {
        return exportPort;
    }

    public void setExportPort(String exportPort) {
        this.exportPort = exportPort;
    }

    public RegisterBean getRegisterBean() {
        return registerBean;
    }

    public void setRegisterBean(RegisterBean registerBean) {
        this.registerBean = registerBean;
    }
}
