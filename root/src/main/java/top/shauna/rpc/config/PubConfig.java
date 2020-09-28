package top.shauna.rpc.config;

import top.shauna.rpc.bean.FoundBean;
import top.shauna.rpc.bean.RegisterBean;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

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
    private FoundBean foundBean;
    private Integer threadPoolNums;
    private Long timeout;
    private Integer maxLen;

    public Integer getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(Integer maxLen) {
        this.maxLen = maxLen;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Integer getThreadPoolNums() {
        return threadPoolNums;
    }

    public void setThreadPoolNums(Integer threadPoolNums) {
        this.threadPoolNums = threadPoolNums;
    }

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

    public FoundBean getFoundBean() {
        return foundBean;
    }

    public void setFoundBean(FoundBean foundBean) {
        this.foundBean = foundBean;
    }

    @Override
    public String toString() {
        return "PubConfig{" +
                "applicationName='" + applicationName + '\'' +
                ", registerBean=" + registerBean +
                ", foundBean=" + foundBean +
                ", threadPoolNums=" + threadPoolNums +
                ", timeout=" + timeout +
                '}';
    }
}
