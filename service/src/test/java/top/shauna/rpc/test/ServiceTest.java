package top.shauna.rpc.test;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.ReferenceBean;
import top.shauna.rpc.bean.RegisterBean;
import top.shauna.rpc.bean.ServiceBean;
import top.shauna.rpc.config.PubConfig;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/11 19:21
 * @E-Mail z1023778132@icloud.com
 */
public class ServiceTest {
    @Test
    public void test() throws Exception {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("META-INF/example.xml");
        System.out.println(PubConfig.getInstance());
        Object bean = classPathXmlApplicationContext.getBean(RegisterBean.class);
        System.out.println(bean);
        Object bean2 = classPathXmlApplicationContext.getBean(LocalExportBean.class);
        System.out.println(bean2);
        Object bean3 = classPathXmlApplicationContext.getBean(ServiceBean.class);
        System.out.println(bean3);
        Object bean4 = classPathXmlApplicationContext.getBean(ReferenceBean.class);
        System.out.println(bean4);
    }
}