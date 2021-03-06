package top.shauna.rpc.finalhelper;

import org.springframework.context.event.ContextRefreshedEvent;
import top.shauna.rpc.bean.FoundBean;
import top.shauna.rpc.bean.RegisterBean;
import top.shauna.rpc.config.PubConfig;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/13 22:26
 * @E-Mail z1023778132@icloud.com
 */
public class FinshAllHelper {

    public static void preparePubConfig(ContextRefreshedEvent event) {
        PubConfig pubConfig = PubConfig.getInstance();
        if (pubConfig.getRegisterBean()==null) {
            RegisterBean registerBean = event.getApplicationContext().containsBean("ShaunaRegister")
                                        ?(RegisterBean) event.getApplicationContext().getBean("ShaunaRegister")
                                        :new RegisterBean("zookeeper","127.0.0.1:2181",null);
            pubConfig.setRegisterBean(registerBean);
        }

        if (pubConfig.getFoundBean()==null) {
            RegisterBean registerBean = pubConfig.getRegisterBean();
            FoundBean foundBean = new FoundBean(
                    registerBean.getPotocol(),
                    registerBean.getUrl(),
                    registerBean.getLoc()
            );
            pubConfig.setFoundBean(foundBean);
        }
    }
}
