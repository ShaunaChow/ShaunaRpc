package top.shauna.rpc.test;

import org.junit.Test;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.RegisterBean;
import top.shauna.rpc.bean.ServiceBean;
import top.shauna.rpc.common.factory.RegistryFactory;
import top.shauna.rpc.common.interfaces.Register;
import top.shauna.rpc.config.PubConfig;
import top.shauna.rpc.supports.ZKSupportKit;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Test1 {

    @Test
    public void test1() throws Exception {
        ServiceBean<?> bean = new ServiceBean<>();
        bean.setInterfaze(Hello.class);
        bean.setInterfaceImpl(null);
        LocalExportBean localExportBean = new LocalExportBean("shauna",8008,"39.105.89.185");
        bean.setLocalExportBean(localExportBean);
        Map<String,Method> map = new HashMap<>();
        for (Method method : Hello.class.getMethods()) {
            map.put(method.getName(),method);
        }
        bean.setMethods(map);
        PubConfig config = PubConfig.getInstance();
        config.setApplicationName("testtttt");
        config.setRegisterBean(new RegisterBean("zookeeper","39.105.89.185:2181",null));
        bean.setPubConfig(config);

        Register register = RegistryFactory.getRegister(bean);
        register.doRegist(bean);

        ZKSupportKit zkSupportKit = new ZKSupportKit("39.105.89.185");
        System.out.println(zkSupportKit.readData("/shauna/top.shauna.rpc.test.Hello/helloCat/providers/39.105.89.185:8008"));

        Class<?> aClass = Class.forName("top.shauna.rpc.bean.LocalExportBean");
        System.out.println(aClass.getName());
        System.out.println(aClass.getMethod("getProtocal").getName());
        LocalExportBean o = (LocalExportBean) aClass.newInstance();
        System.out.println(o);

        System.in.read();
    }
}
