package top.shauna.rpc.test;

import org.junit.Test;
import top.shauna.rpc.bean.FoundBean;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.RegisterBean;
import top.shauna.rpc.config.PubConfig;
import top.shauna.rpc.service.ShaunaRPCHandler;
import top.shauna.rpc.test.impl.HelloImpl;

import java.io.IOException;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/25 15:27
 * @E-Mail z1023778132@icloud.com
 */
public class Test1 {

    @Test
    public void test1() throws IOException {
        LocalExportBean localExportBean = new LocalExportBean();
        localExportBean.setProtocol("netty");
        localExportBean.setIp("127.0.0.1");
        localExportBean.setPort(8088);

        PubConfig pubConfig = PubConfig.getInstance();
        if (pubConfig.getRegisterBean()==null) {
            RegisterBean registerBean = new RegisterBean("zookeeper","39.105.89.185:2181",null);
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

        ShaunaRPCHandler.publishServiceBean(Hello.class, new HelloImpl(),localExportBean);

        //top.shauna.rpc.test.Hello
        System.in.read();
    }

    @Test
    public void test2() throws IOException {
        PubConfig pubConfig = PubConfig.getInstance();
        pubConfig.setTimeout(10000000L);
        if (pubConfig.getRegisterBean()==null) {
            RegisterBean registerBean = new RegisterBean("zookeeper","39.105.89.185:2181",null);
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

        Hello hello = ShaunaRPCHandler.getReferenceProxy(Hello.class);

        System.out.println(hello.helloCat("mago", new LocalExportBean()));
        byte[] bytes = hello.okkk();
        for (byte aByte : bytes) {
            System.out.print(aByte+" ");
        }
        System.out.println();
        //top.shauna.rpc.test.Hello
        System.in.read();
    }

    @Test
    public void test3() throws IOException {
        System.out.println(byte[].class);
    }
}
