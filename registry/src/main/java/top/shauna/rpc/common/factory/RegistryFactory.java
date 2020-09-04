package top.shauna.rpc.common.factory;

import top.shauna.rpc.bean.ServiceBean;
import top.shauna.rpc.common.interfaces.Register;

public class RegistryFactory {

    public static Register getRegister(ServiceBean<?> serviceBean) throws Exception {
        /** 使用用户自定义的Register类 **/
        String loc = serviceBean.getPubConfig().getRegisterBean().getLoc();
        String url = serviceBean.getPubConfig().getRegisterBean().getUrl();
        if(loc!=null){
            Class clazz = Class.forName(loc);
            Object register = clazz.newInstance();
            if(register instanceof Register){
                ((Register) register).connect(url);
                return (Register) register;
            }else throw new Exception("指定的类必须实现Register接口!!!");
        }
        /** 使用指定的系统类 **/
        String register = serviceBean.getPubConfig().getRegisterBean().getPotocol();
        if(register==null) register = "zookeeper";
        String className = "top.shauna.rpc.common.register."
                +register.substring(0,1).toUpperCase()
                +register.substring(1)+"Register";
        Class clazz = Class.forName(className);
        Object obj = clazz.newInstance();
        if(obj instanceof Register){
            ((Register) obj).connect(url);
            return (Register) obj;
        }else throw new Exception("指定的类必须实现Register接口!!!");
    }
}
