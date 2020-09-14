package top.shauna.rpc.test;

import com.alibaba.fastjson.JSON;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.RegisterBean;
import top.shauna.rpc.bean.ServiceBean;
import top.shauna.rpc.common.interfaces.Founder;
import top.shauna.rpc.config.PubConfig;
import top.shauna.rpc.test.services.Hello;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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

        System.out.println("=====================");

        Class interfaze = Founder.class;
        Object bean1 = classPathXmlApplicationContext.getBean(interfaze);
        System.out.println(bean1);

    }

    @Test
    public void test2() throws Exception {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("META-INF/example.xml");

        LocalExportBean bean = classPathXmlApplicationContext.getBean(LocalExportBean.class);

        Hello hello = classPathXmlApplicationContext.getBean(Hello.class);

        while(true) {
            try {
                System.out.println(hello.hello("mago"));
                System.out.println(hello.hello(bean));
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(hello);
                Thread.sleep(2000);
            }
        }
        hello.ok(200);
        System.out.println(hello.ok(203, 432));
        System.out.println(hello.okk(98));
    }

    @Test
    public void test3(){
        System.out.println(JSON.toJSONString("123"));
        System.out.println(JSON.parseObject( JSON.toJSONString("123").toString() , String.class));
        System.out.println(JSON.parseObject("123" , String.class));
        for (Method method : Hello.class.getMethods()) {
            StringBuilder sb = new StringBuilder(method.getReturnType().getName()+" "+method.getName()+"(");
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                if(i==method.getParameters().length-1){
                    sb.append(parameter.getType().getName()+")");
                }else sb.append(parameter.getType().getName()+" ,");
            }
            System.out.println(sb.toString());
        }
    }
}