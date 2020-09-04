package top.shauna.rpc.common.register;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.shauna.rpc.common.interfaces.Register;
import top.shauna.rpc.supports.ZKSupportKit;


/**
 * Zk注测组件
 */
public class ZookeeperRegister implements Register {

    private String urlPatten = "/shauna/";
    private ZKSupportKit zkSupportKit;
    private static Logger log = LogManager.getLogger(ZookeeperRegister.class);

    public ZookeeperRegister(String zkServers){
        zkSupportKit = new ZKSupportKit(zkServers);
    }

    public ZookeeperRegister(){}

    @Override
    public boolean isValid(String url) {
        return url.startsWith(urlPatten);
    }

    @Override
    public void doRegist(String url,boolean isProvider) {
        if (isValid(url)){
            int pin = url.lastIndexOf('/');
            String node = url.substring(0,pin);
            String context = url.substring(pin+1);
            if (!zkSupportKit.isExits(node)) {
                zkSupportKit.create(node,context,true);
            }else{
                zkSupportKit.writeData(node,context);
            }
        }else{
            log.error("url不符合规范!!!");
        }
    }

    @Override
    public void connect(String url) {
        zkSupportKit = new ZKSupportKit(url,5000);
    }

    public static void main(String[] args) {
        String url = "1234/567/89";
        System.out.println(url.lastIndexOf('/'));
    }
}
