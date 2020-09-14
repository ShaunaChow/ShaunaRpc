package top.shauna.rpc.factorybeans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import top.shauna.rpc.bean.FoundBean;
import top.shauna.rpc.bean.ReferenceBean;
import top.shauna.rpc.bean.RegisterBean;
import top.shauna.rpc.common.factory.FounderFactory;
import top.shauna.rpc.common.interfaces.Founder;
import top.shauna.rpc.config.PubConfig;
import top.shauna.rpc.finalhelper.FinshAllHelper;
import top.shauna.rpc.holder.ConnecterHolder;
import top.shauna.rpc.proxy.ReferenceProxyFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/14 11:40
 * @E-Mail z1023778132@icloud.com
 */
@Slf4j
public class ReferenceBeanFactory implements FactoryBean, ApplicationListener<ContextRefreshedEvent> {
    private ReferenceBean bean;

    @Override
    public Class<?> getObjectType() {
        return bean.getInterfaze();
    }

    @Override
    public Object getObject() {
        return dealWithReferenceBean(bean);
    }

    private Object dealWithReferenceBean(ReferenceBean bean) {
        return ReferenceProxyFactory.getProxy(bean);
    }

    public ReferenceBean getBean() {
        return bean;
    }

    public void setBean(ReferenceBean bean) {
        this.bean = bean;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        FinshAllHelper.preparePubConfig(event);
        String interfazeName = bean.getClassName();
        if(interfazeName==null){
            log.error("ReferenceBean必须指定一个接口");
            return;
        }
        if(bean.getInterfaze()==null) {
            try {
                Class interfaze = Class.forName(interfazeName);
                bean.setInterfaze(interfaze);
            } catch (ClassNotFoundException e) {
                log.error("ReferenceBean解析接口时出错：" + e.getMessage());
                return;
            }
        }
        /** bean的初始化工作 **/
        {
            bean.setLocalExportAddrList(new CopyOnWriteArrayList<>());
            bean.setLocalExportBeanList(new CopyOnWriteArrayList<>());
            bean.setRemoteClients(new CopyOnWriteArrayList<>());
            ConnecterHolder.put(interfazeName,bean);
        }
        try {
            Founder founder = FounderFactory.getFounder();
            founder.found(bean.getInterfaze());
            founder.listen(bean.getInterfaze());
            log.info("服务发现ok");
        } catch (Exception e) {
            log.error(bean.getClassName()+" 服务发现失败："+e.getMessage());
        }
    }
}
