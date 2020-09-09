package top.shauna.rpc.loadbalance;

import top.shauna.rpc.bean.ReferenceBean;
import top.shauna.rpc.bean.RemoteClient;
import top.shauna.rpc.interfaces.LoadBalance;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class ShaunaPollBalance implements LoadBalance {
    private static int pin;
    private static String id;

    public ShaunaPollBalance() {
        pin = 0;
    }

    @Override
    public RemoteClient getRemoteClient(ReferenceBean referenceBean) {
        if (referenceBean.getRemoteClients().size()==0) return null;
        else return referenceBean.getRemoteClients().get(pin%referenceBean.getRemoteClients().size());
    }
}
