package top.shauna.rpc.loadbalance;

import top.shauna.rpc.bean.InvokeInfo;
import top.shauna.rpc.bean.RemoteClient;
import top.shauna.rpc.interfaces.LoadBalance;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author Shauna.Chow
 * @Date 2021/2/22 15:45
 * @E-Mail z1023778132@icloud.com
 */
public class LeastActiveLoadBalance implements LoadBalance {

    public LeastActiveLoadBalance() {
    }

    @Override
    public RemoteClient getRemoteClient(CopyOnWriteArrayList<RemoteClient> list, InvokeInfo invokeInfo) {
        list.sort((c1,c2)-> c1.getAvgTimeMills()>c2.getAvgTimeMills()?1:-1);
        for(RemoteClient client:list){
            System.out.println(client.getPort()+" "+client.getAvgTimeMills());
        }
        for(RemoteClient client:list){
            if(client.getChannel().isOk()) return client;
        }
        return null;
    }
}
