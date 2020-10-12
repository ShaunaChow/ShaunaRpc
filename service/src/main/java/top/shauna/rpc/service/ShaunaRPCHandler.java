package top.shauna.rpc.service;

import lombok.extern.slf4j.Slf4j;
import top.shauna.rpc.bean.*;
import top.shauna.rpc.client.ClientFactory;
import top.shauna.rpc.common.factory.FounderFactory;
import top.shauna.rpc.common.factory.RegistryFactory;
import top.shauna.rpc.common.interfaces.Founder;
import top.shauna.rpc.common.interfaces.Register;
import top.shauna.rpc.config.PubConfig;
import top.shauna.rpc.holder.MethodsHolder;
import top.shauna.rpc.interfaces.ClientStarter;
import top.shauna.rpc.interfaces.LocalExporter;
import top.shauna.rpc.proxy.ReferenceProxyFactory;
import top.shauna.rpc.server.ExportFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/25 15:00
 * @E-Mail z1023778132@icloud.com
 */
@Slf4j
public class ShaunaRPCHandler {

    public static <T> T getReferenceProxy(Class interfaze){
        ReferenceBean referenceBean = new ReferenceBean();
        referenceBean.setInterfaze(interfaze);
        referenceBean.setClassName(interfaze.getName());
        referenceBean.setRemoteClients(new CopyOnWriteArrayList<>());
        referenceBean.setLocalExportBeanList(new CopyOnWriteArrayList<>());
        referenceBean.setLocalExportAddrList(new CopyOnWriteArrayList<>());

        preparePubConfig();

        try {
            Founder founder = FounderFactory.getFounder();
            founder.found(referenceBean);
            founder.listen(referenceBean);
            log.info("服务发现ok");
        } catch (Exception e) {
            log.error(referenceBean.getClassName()+" 服务发现失败："+e.getMessage());
        }

        return ReferenceProxyFactory.getProxy(referenceBean);
    }

    public static <T> T getReferenceProxy(Class interfaze,LocalExportBean localExportBean) throws Exception {
        ReferenceBean referenceBean = new ReferenceBean();
        referenceBean.setInterfaze(interfaze);
        referenceBean.setClassName(interfaze.getName());
        referenceBean.setRemoteClients(new CopyOnWriteArrayList<>());
        referenceBean.setLocalExportBeanList(new CopyOnWriteArrayList<>());
        referenceBean.setLocalExportAddrList(new CopyOnWriteArrayList<>());

        ClientStarter clientStarter = ClientFactory.getClientStarter(localExportBean);
        clientStarter.connect(localExportBean,referenceBean);

        return ReferenceProxyFactory.getProxy(referenceBean);
    }

    public static void publishServiceBean(Class interfaze, Object impl, LocalExportBean localExportBean){
        boolean flag = false;
        for (Class<?> interfazee : impl.getClass().getInterfaces()) {
            if(interfazee==interfaze) flag = true;
        }
        if (!flag) {
            log.error("实现类"+impl+"没有实现指定接口："+interfaze.getName());
            return;
        }

        preparePubConfig();

        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setInterfaceImpl(impl);
        serviceBean.setInterfaze(interfaze);
        serviceBean.setRef(impl.getClass().getName());
        serviceBean.setLocalExportBean(localExportBean);
        ConcurrentHashMap<String,Method> methods = new ConcurrentHashMap<>();
        for (Method method : interfaze.getMethods()) {
            methods.put(getMethodFullName(method),method);
        }
        serviceBean.setMethods(methods);
        MethodsHolder.putMethod(interfaze.getName(), serviceBean);

        doExport(serviceBean);
        doRegister(serviceBean);
    }

    private static void doRegister(ServiceBean bean) {
        try {
            Register register = RegistryFactory.getRegister();
            register.doRegist(bean);
        } catch (Exception e) {
            log.error("bean"+bean.getInterfaze().getName()+"注测失败:"+e.getMessage());
        }
    }

    private static void doExport(ServiceBean bean) {
        try {
            LocalExporter exporter = ExportFactory.getExporter(bean);
            exporter.init(bean.getLocalExportBean());
        } catch (Exception e) {
            log.error("bean"+bean.getInterfaze().getName()+"暴露失败:"+e.getMessage());
        }
    }

    private static String getMethodFullName(Method method) {
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

    private static void preparePubConfig(){
        PubConfig pubConfig = PubConfig.getInstance();
        if (pubConfig.getRegisterBean()==null) {
            RegisterBean registerBean = new RegisterBean("zookeeper","127.0.0.1:2181",null);
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
