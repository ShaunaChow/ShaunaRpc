package top.shauna.rpc.common.factory;

import top.shauna.rpc.common.interfaces.Founder;
import top.shauna.rpc.config.PubConfig;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class FounderFactory {

    public static Founder getFounder() throws Exception {
        /** 使用用户自定义的Register类 **/
        String loc = PubConfig.getInstance().getFoundBean().getLoc();
        String url = PubConfig.getInstance().getFoundBean().getUrl();
        if(loc!=null){
            Class clazz = Class.forName(loc);
            Object founder = clazz.newInstance();
            if(founder instanceof Founder){
                ((Founder) founder).connect(url);
                return (Founder) founder;
            }else throw new Exception("founder指定的类必须实现Founder接口!!!");
        }
        /** 使用指定的系统类 **/
        String founder = PubConfig.getInstance().getFoundBean().getPotocol();
        if(founder==null) founder = "zookeeper";
        String className = "top.shauna.rpc.common.found."
                +founder.substring(0,1).toUpperCase()
                +founder.substring(1)+"Founder";
        Class clazz = Class.forName(className);
        Object obj = clazz.newInstance();
        if(obj instanceof Founder){
            ((Founder) obj).connect(url);
            return (Founder) obj;
        }else throw new Exception("Founder指定的类必须实现Founder接口!!!");
    }
}
