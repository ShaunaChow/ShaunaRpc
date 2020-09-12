package top.shauna.rpc.insertinvokers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.ReferenceBean;
import top.shauna.rpc.bean.ServiceBean;
import top.shauna.rpc.common.factory.RegistryFactory;
import top.shauna.rpc.common.interfaces.Register;
import top.shauna.rpc.holder.MethodsHolder;
import top.shauna.rpc.interfaces.LocalExporter;
import top.shauna.rpc.server.ExportFactory;

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
            dealWithServiceBean((ServiceBean) bean);
            System.out.println(bean);
        }else if(bean instanceof ReferenceBean){
            System.out.println(bean);
        }
        return bean;
    }

    private void dealWithServiceBean(ServiceBean bean){
        Class interfaze = bean.getInterfaze();
        Object impl = null;
        if (applicationContext.containsBean(bean.getRef())) {
            impl = applicationContext.getBean(bean.getRef());
        }
        if(impl==null){
            try {
                impl = Class.forName(bean.getRef()).newInstance();
            } catch (Exception e) {
                log.error("ServiceBean的实现类解析错误 "+e.getMessage());
            }
        }
        if(!isValid(impl,interfaze)){
            log.error("ServiceBean的实现类没有继承指定接口");
            return;
        }
        bean.setInterfaceImpl(impl);
        Method[] interfaceMethods = interfaze.getMethods();
        ConcurrentHashMap<String,Method> methods = new ConcurrentHashMap<>();
        for(Method method:interfaceMethods){
            methods.put(method.getName(),method);
        }
        bean.setMethods(methods);
        LocalExportBean localExportBean = null;
        if (applicationContext.containsBean("ShaunaLocalExport")) {
            localExportBean = (LocalExportBean) applicationContext.getBean("ShaunaLocalExport");
            bean.setLocalExportBean(localExportBean);
        }
        MethodsHolder.putMethod(interfaze.getName(), bean);
        doExport(bean);
        doRegister(bean);
    }

    private void doRegister(ServiceBean bean) {
        try {
            Register register = RegistryFactory.getRegister();
            register.doRegist(bean);
        } catch (Exception e) {
            log.error("bean"+bean.getInterfaze().getName()+"注测失败:"+e.getMessage());
        }
    }

    private void doExport(ServiceBean bean){
        try {
            LocalExporter exporter = ExportFactory.getExporter(bean);
            exporter.init(bean.getLocalExportBean());
        } catch (Exception e) {
            log.error("bean"+bean.getInterfaze().getName()+"暴露失败:"+e.getMessage());
        }
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
