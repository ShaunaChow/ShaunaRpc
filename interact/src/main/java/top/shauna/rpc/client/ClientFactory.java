package top.shauna.rpc.client;

import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.interfaces.ClientStarter;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class ClientFactory {

    public static ClientStarter getClientStarter(LocalExportBean localExportBean) throws Exception {
        String loc = localExportBean.getClientClassLoc();
        if(loc!=null){
            Class clazz = Class.forName(loc);
            Object starter = clazz.newInstance();
            if(starter instanceof ClientStarter){
                return (ClientStarter) starter;
            }else throw new Exception("ClientStarter指定的类必须实现ClientStarter接口!!!");
        }

        String protocal = localExportBean.getProtocal();
        if(protocal==null) protocal = "netty";
        String className = "top.shauna.rpc.client.connecter."
                +protocal.substring(0,1).toUpperCase()
                +protocal.substring(1)+"ClientStarter";
        Class clazz = Class.forName(className);
        Object obj = clazz.newInstance();
        if(obj instanceof ClientStarter){
            return (ClientStarter) obj;
        }else throw new Exception("指定的类必须实现Register接口!!!");
    }
}
