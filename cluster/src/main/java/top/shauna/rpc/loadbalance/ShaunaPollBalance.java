package top.shauna.rpc.loadbalance;

import top.shauna.rpc.bean.ReferenceBean;
import top.shauna.rpc.bean.RemoteClient;
import top.shauna.rpc.interfaces.LoadBalance;

public class ShaunaPollBalance implements LoadBalance {
    private static int pin;

    public ShaunaPollBalance() {
        pin = 0;
    }

    @Override
    public RemoteClient getRemoteClient(ReferenceBean referenceBean) {
        return referenceBean.getRemoteClients().get(pin%referenceBean.getRemoteClients().size());
    }
}
