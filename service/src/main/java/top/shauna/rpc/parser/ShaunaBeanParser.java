package top.shauna.rpc.parser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import top.shauna.rpc.bean.RegisterBean;
import top.shauna.rpc.config.PubConfig;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/10 21:10
 * @E-Mail z1023778132@icloud.com
 */
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
        }
        return null;
    }

    private static void preparePubConfig(Element element){
        PubConfig.getInstance().setApplicationName(
                element.getAttribute("appname")==null?
                        "ShaunaApplication":
                        element.getAttribute("appname")
        );
        int cores;
        if (element.getAttribute("threadnums")==null) {
            cores = Runtime.getRuntime().availableProcessors()*2;
        }else{
            cores = Integer.parseInt(element.getAttribute("threadnums"));
        }
        PubConfig.getInstance().setThreadPoolNums(cores);
        Long timeout;
        if (element.getAttribute("threadnums")==null) {
            timeout = 5000L;
        }else{
            timeout = Long.parseLong(element.getAttribute("timeout"));
        }
        PubConfig.getInstance().setTimeout(timeout);
    }
}
