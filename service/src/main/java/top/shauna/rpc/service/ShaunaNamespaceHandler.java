package top.shauna.rpc.service;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.ReferenceBean;
import top.shauna.rpc.bean.RegisterBean;
import top.shauna.rpc.bean.ServiceBean;
import top.shauna.rpc.config.PubConfig;
import top.shauna.rpc.parser.ShaunaBeanParser;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/10 15:05
 * @E-Mail z1023778132@icloud.com
 */
public class ShaunaNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("pubconfig", new ShaunaBeanParser(PubConfig.class));
        registerBeanDefinitionParser("register", new ShaunaBeanParser(RegisterBean.class));
        registerBeanDefinitionParser("export", new ShaunaBeanParser(LocalExportBean.class));
        registerBeanDefinitionParser("service", new ShaunaBeanParser(ServiceBean.class));
        registerBeanDefinitionParser("reference", new ShaunaBeanParser(ReferenceBean.class));
    }
}
