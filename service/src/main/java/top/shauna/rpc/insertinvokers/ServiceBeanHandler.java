package top.shauna.rpc.insertinvokers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import top.shauna.rpc.bean.*;
import top.shauna.rpc.common.factory.FounderFactory;
import top.shauna.rpc.common.factory.RegistryFactory;
import top.shauna.rpc.common.interfaces.Founder;
import top.shauna.rpc.common.interfaces.Register;
import top.shauna.rpc.config.PubConfig;
import top.shauna.rpc.holder.MethodsHolder;
import top.shauna.rpc.interfaces.LocalExporter;
import top.shauna.rpc.server.ExportFactory;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
        }else if(bean instanceof ReferenceBean){
            dealWithReferenceBean((ReferenceBean) bean);
        }else if(bean instanceof RegisterBean){
            dealWithRegisterBean((RegisterBean) bean);
        }else if(bean instanceof LocalExportBean){
            dealWithLocalExportBean((LocalExportBean) bean);
        }else if(bean instanceof FoundBean){
            dealWithFoundBean((FoundBean) bean);
        }
        return bean;
    }

    private void dealWithFoundBean(FoundBean bean) {
        PubConfig.getInstance().setFoundBean(bean);
    }

    private void dealWithLocalExportBean(LocalExportBean bean) {
        /** To Be Finshed!!! */
    }

    private void dealWithRegisterBean(RegisterBean bean) {
        PubConfig.getInstance().setRegisterBean(bean);
    }

    private void dealWithReferenceBean(ReferenceBean bean){
        try {
            Founder founder = FounderFactory.getFounder();
            founder.found(bean);
            founder.listen(bean);
        } catch (Exception e) {
            log.error("ReferenceBean:"+bean.getClassName()+"服务发现出错："+e.getMessage());
            return;
        }
    }

    private void dealWithServiceBean(ServiceBean bean){
        Class interfaze = bean.getInterfaze();
        Object impl = null;
        if (bean.getRef()!=null&&applicationContext.containsBean(bean.getRef())) {
            impl = applicationContext.getBean(bean.getRef());
        }
        if(impl==null){
            try {
                impl = Class.forName(bean.getRef()).newInstance();
            } catch (Exception e) {
                log.error("ServiceBean的实现类解析错误 "+e.getMessage());
                return;
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
            methods.put(getMethodFullName(method),method);
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

    private String getMethodFullName(Method method) {
        StringBuilder sb = new StringBuilder(method.getReturnType().getName()+" "+method.getName()+"(");
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if(i==method.getParameters().length-1){
                sb.append(parameter.getType().getName()+")");
            }else sb.append(parameter.getType().getName()+" ,");
        }
        return sb.toString();
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
