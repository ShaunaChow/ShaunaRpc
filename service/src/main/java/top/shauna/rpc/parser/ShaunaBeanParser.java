package top.shauna.rpc.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.ReferenceBean;
import top.shauna.rpc.bean.RegisterBean;
import top.shauna.rpc.bean.ServiceBean;
import top.shauna.rpc.config.PubConfig;
import top.shauna.rpc.insertinvokers.ServiceBeanHandler;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/10 21:10
 * @E-Mail z1023778132@icloud.com
 */
@Slf4j
public class ShaunaBeanParser implements BeanDefinitionParser {
    private Class tar;

    public ShaunaBeanParser(Class tar){
        this.tar = tar;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        if(tar==PubConfig.class) {
            preparePubConfig(element);
        }else if(tar==RegisterBean.class){
            RootBeanDefinition register = new RootBeanDefinition(tar);
            register.setLazyInit(false);
            if(element.hasAttribute("protocol")){
                register.getPropertyValues().addPropertyValue("potocol",element.getAttribute("protocol"));
            }else{
                register.getPropertyValues().addPropertyValue("potocol",element.getAttribute("netty"));
            }
            if(element.hasAttribute("url")){
                register.getPropertyValues().addPropertyValue("url",element.getAttribute("url"));
            }
            if(element.hasAttribute("loc")){
                register.getPropertyValues().addPropertyValue("loc",element.getAttribute("loc"));
            }
            parserContext.getRegistry().registerBeanDefinition("ShaunaRegister",register);
            return register;
        }else if(tar==LocalExportBean.class){
            RootBeanDefinition export = new RootBeanDefinition(tar);
            export.setLazyInit(false);
            if(element.hasAttribute("protocol")){
                export.getPropertyValues().addPropertyValue("protocol",element.getAttribute("protocol"));
            }else{
                export.getPropertyValues().addPropertyValue("protocol",element.getAttribute("netty"));
            }
            if(element.hasAttribute("ip")){
                export.getPropertyValues().addPropertyValue("ip",element.getAttribute("ip"));
            }
            if(element.hasAttribute("port")){
                export.getPropertyValues().addPropertyValue("port",element.getAttribute("port"));
            }
            if(element.hasAttribute("serverclassloc")){
                export.getPropertyValues().addPropertyValue("serverClassLoc",element.getAttribute("serverclassloc"));
            }
            if(element.hasAttribute("clientclassloc")){
                export.getPropertyValues().addPropertyValue("clientClassLoc",element.getAttribute("clientclassloc"));
            }
            parserContext.getRegistry().registerBeanDefinition("ShaunaLocalExport",export);
            return export;
        }else if(tar==ServiceBean.class){
            RootBeanDefinition service = new RootBeanDefinition(tar);
            service.setLazyInit(false);
            String clazz = null;
            if(element.hasAttribute("interface")){
                try{
                    clazz = element.getAttribute("interface");
                    Class interfaze = Class.forName(clazz);
                    service.getPropertyValues().addPropertyValue("interfaze",interfaze);
                }catch (Exception e){
                    log.error("shauna:service定义的接口未找到!");
                }
            }else{
                log.error("shauna:service接口未给出定义");
            }
            if(element.hasAttribute("ref")){
                service.getPropertyValues().addPropertyValue("ref",element.getAttribute("ref"));
            }else{
                log.error("shauna:service接口实现类必须给出!!");
            }
            parserContext.getRegistry().registerBeanDefinition("ShaunaService:"+clazz,service);
            return service;
        }else if(tar==ReferenceBean.class){
            RootBeanDefinition reference = new RootBeanDefinition(tar);
            reference.setLazyInit(false);
            String clazz = null;
            if(element.hasAttribute("interface")){
                try{
                    clazz = element.getAttribute("interface");
                    Class interfaze = Class.forName(clazz);
                    reference.getPropertyValues().addPropertyValue("interfaze",interfaze);
                    reference.getPropertyValues().addPropertyValue("className",clazz);
                }catch (Exception e){
                    log.error("shauna:reference定义的接口未找到!");
                }
            }else{
                log.error("shauna:reference接口未给出定义");
            }
            parserContext.getRegistry().registerBeanDefinition("ShaunaReference:"+clazz,reference);
            return reference;
        }
        if(!parserContext.getRegistry().containsBeanDefinition("ServiceBeanHandler")){
            parserContext.getRegistry().registerBeanDefinition(
                    "ServiceBeanHandler",
                    new RootBeanDefinition(ServiceBeanHandler.class));
        }
        return null;
    }

    private static void preparePubConfig(Element element){
        PubConfig.getInstance().setApplicationName(
                element.hasAttribute("appname")?
                element.getAttribute("appname"):
                "ShaunaApplication"
        );
        int cores;
        if (!element.hasAttribute("threadnums")) {
            cores = Runtime.getRuntime().availableProcessors()*2;
        }else{
            cores = Integer.parseInt(element.getAttribute("threadnums"));
        }
        PubConfig.getInstance().setThreadPoolNums(cores);
        Long timeout;
        if (!element.hasAttribute("threadnums")) {
            timeout = 5000L;
        }else{
            timeout = Long.parseLong(element.getAttribute("timeout"));
        }
        PubConfig.getInstance().setTimeout(timeout);
    }
}
