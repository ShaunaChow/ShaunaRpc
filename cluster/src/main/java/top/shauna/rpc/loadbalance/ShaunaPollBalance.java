package top.shauna.rpc.loadbalance;

import top.shauna.rpc.bean.RemoteClient;
import top.shauna.rpc.interfaces.LoadBalance;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class ShaunaPollBalance implements LoadBalance {
    private static int pin = 0;
    private static String id;

    public ShaunaPollBalance() {
    }

    @Override
    public RemoteClient getRemoteClient(CopyOnWriteArrayList<RemoteClient> remoteClients) {
        if (remoteClients.size()==0) return null;
        else return remoteClients.get(pin++%remoteClients.size());
    }
}
