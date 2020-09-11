package top.shauna.rpc.insertinvokers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import top.shauna.rpc.bean.ServiceBean;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/11 22:05
 * @E-Mail z1023778132@icloud.com
 */
@Slf4j
public class ServiceBeanHandler implements BeanPostProcessor, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof ServiceBean){
            Class interfaze = ((ServiceBean) bean).getInterfaze();
            Object impl = null;
            if (applicationContext.containsBean(((ServiceBean) bean).getRef())) {
                impl = applicationContext.getBean(((ServiceBean) bean).getRef());
            }
            if(impl==null){
                try {
                    impl = Class.forName(((ServiceBean) bean).getRef()).newInstance();
                } catch (Exception e) {
                    log.error("ServiceBean的实现类解析错误 "+e.getMessage());
                }
            }
            if(!isValid(impl,interfaze)){
                log.error("ServiceBean的实现类没有继承指定接口");
                return bean;
            }
            ((ServiceBean) bean).setInterfaceImpl(impl);
            Method[] interfaceMethods = interfaze.getMethods();
            ConcurrentHashMap<String,Method> methods = new ConcurrentHashMap<>();
            for(Method method:interfaceMethods){
                methods.put(method.getName(),method);
            }
            ((ServiceBean) bean).setMethods(methods);
        }
        return bean;
    }

    private boolean isValid(Object impl, Class interfaze) {
        Class<?>[] interfaces = impl.getClass().getInterfaces();
        for(Class clazz:interfaces){
            if(clazz==interfaze) return true;
        }
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
