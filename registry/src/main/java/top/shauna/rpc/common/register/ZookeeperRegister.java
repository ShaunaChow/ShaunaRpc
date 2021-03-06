package top.shauna.rpc.common.register;

import com.alibaba.fastjson.JSON;
import top.shauna.rpc.bean.ServiceBean;
import top.shauna.rpc.common.interfaces.Register;
import top.shauna.rpc.supports.ZKSupportKit;



/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class ZookeeperRegister implements Register {

    private String urlPatten = "/shauna/";
    private ZKSupportKit zkSupportKit;

    public ZookeeperRegister(String zkServers){
        zkSupportKit = new ZKSupportKit(zkServers);
    }

    public ZookeeperRegister(){}

    @Override
    public boolean isValid(String url) {
        return url.startsWith(urlPatten);
    }

    @Override
    public void doRegist(ServiceBean<?> serviceBean) {
        String pre = "/shauna/";
        String classFullName = serviceBean.getInterfaze().getName();
        String providerIP = serviceBean.getLocalExportBean().getIp();
        int providerPort = serviceBean.getLocalExportBean().getPort();
        String providerProtocal = JSON.toJSONString(serviceBean.getLocalExportBean());
        String url = pre + classFullName + "/"
                + "providers/" + providerIP
                + ":" + providerPort;
        doRegist(url,providerProtocal,true);

    }

    public void doRegist(String url, String context, boolean isEphemeral) {
        int pin = url.lastIndexOf('/');
        if(pin==0) {
            /** 创建根节点 **/
            zkSupportKit.create(url,context,false);
            return;
        }
        String fatherNode = url.substring(0,pin);
        if (!zkSupportKit.isExits(fatherNode)) {
            /**创建父节点**/
            doRegist(fatherNode,null,false);
        }
        if(zkSupportKit.isExits(url))
            zkSupportKit.writeData(url,context);
        else
            zkSupportKit.create(url,context,isEphemeral);
    }

    @Override
    public void connect(String url) {
        zkSupportKit = new ZKSupportKit(url,5000);
    }
}
