package top.shauna.rpc.config;

import top.shauna.rpc.bean.RegisterBean;

public class PubConfig {

    private PubConfig(){}

    private static volatile PubConfig pubConfig;

    public static PubConfig getInstance(){
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

    public RegisterBean getRegisterBean() {
        return registerBean;
    }

    public void setRegisterBean(RegisterBean registerBean) {
        this.registerBean = registerBean;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}