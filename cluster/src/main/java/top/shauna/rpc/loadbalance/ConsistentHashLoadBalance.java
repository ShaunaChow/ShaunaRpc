package top.shauna.rpc.loadbalance;

import lombok.extern.slf4j.Slf4j;
import top.shauna.rpc.bean.InvokeInfo;
import top.shauna.rpc.bean.RemoteClient;
import top.shauna.rpc.interfaces.LoadBalance;

import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author Shauna.Chow
 * @Date 2021/2/22 14:50
 * @E-Mail z1023778132@icloud.com
 */
@Slf4j
public class ConsistentHashLoadBalance implements LoadBalance {

    public ConsistentHashLoadBalance() {
    }

    @Override
    public RemoteClient getRemoteClient(CopyOnWriteArrayList<RemoteClient> list, InvokeInfo invokeInfo) {
        if (list==null||list.isEmpty()) return null;
        list.sort(Comparator.comparing(Object::hashCode));
        int hashCode = invokeInfo.hashCode();
        for (RemoteClient client:list){
            if (!client.getChannel().isOk()) continue;
            if(client.hashCode()>hashCode) return client;
        }
        RemoteClient client = list.get(0);
        return client.getChannel().isOk()?client:null;
    }
}
