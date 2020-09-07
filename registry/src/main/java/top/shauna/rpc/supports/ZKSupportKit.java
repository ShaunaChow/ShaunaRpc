package top.shauna.rpc.supports;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

import java.util.List;

public class ZKSupportKit {

    private static ZkClient zkClient;

    public ZKSupportKit(String zkServers, int connectionTimeout){
        zkClient = new ZkClient(zkServers,connectionTimeout);
    }

    public ZKSupportKit(String zkServers){
        zkClient = new ZkClient(zkServers);
    }

    public void create(String path, String content, boolean ephemeral){
        if (ephemeral)
            zkClient.createEphemeral(path,content);
        else
            zkClient.create(path,content,CreateMode.PERSISTENT);
    }

    public boolean delete(String path){
        return zkClient.delete(path);
    }

    public String readData(String path){
        return zkClient.readData(path);
    }

    public List<String> readChildren(String path){
        return zkClient.getChildren(path);
    }

    public boolean isExits(String path){
        return zkClient.exists(path);
    }

    public void writeData(String path, String data){
        zkClient.writeData(path,data);
    }

    public void subscribeDataChanges(String path, IZkDataListener zkDataListener){
        zkClient.subscribeDataChanges(path,zkDataListener);
    }

    public void subscribeChildChanges(String path, IZkChildListener zkChildListener){
        zkClient.subscribeChildChanges(path, zkChildListener);
    }
}
