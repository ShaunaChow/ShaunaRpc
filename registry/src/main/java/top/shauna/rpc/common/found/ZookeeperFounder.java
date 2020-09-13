package top.shauna.rpc.common.found;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.ReferenceBean;
import top.shauna.rpc.bean.RemoteClient;
import top.shauna.rpc.client.ClientFactory;
import top.shauna.rpc.common.interfaces.Founder;
import top.shauna.rpc.holder.ConnecterHolder;
import top.shauna.rpc.interfaces.ClientStarter;
import top.shauna.rpc.supports.ZKSupportKit;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class ZookeeperFounder implements Founder {
    private ZKSupportKit zkSupportKit;

    @Override
    public void listen(Class interfaze) {
        zkSupportKit.subscribeChildChanges(getZookeeperPath(interfaze), (parentPath, currentChilds) -> doChange(parentPath,currentChilds));
    }

    private String getZookeeperPath(Class interfaze) {
        return "/shauna/"+interfaze.getName()+"/providers";
    }

    /**
     * @Param: path 约定为/shauna/className/providers
     * **/
    @Override
    public void found(Class interfaze) throws Exception {
        String className = interfaze.getName();
        ReferenceBean referenceBean = ConnecterHolder.get(className);
        String path = getZookeeperPath(interfaze);
        doPut(referenceBean,path,new CopyOnWriteArraySet(zkSupportKit.readChildren(path)));
    }

    private void doChange(String path, List<String> currentChilds) throws Exception {
        if(!path.startsWith("/shauna/")) throw new Exception("发现功能监听路径错误 [doChange]");
        String className = path.substring(8);
        className = className.substring(0,className.indexOf("/"));
        ReferenceBean referenceBean = ConnecterHolder.get(className);
        CopyOnWriteArrayList<String> localExportAddrList = referenceBean.getLocalExportAddrList();
        CopyOnWriteArraySet alreadyIn;
        if(localExportAddrList==null) alreadyIn = new CopyOnWriteArraySet();
        else alreadyIn = new CopyOnWriteArraySet(localExportAddrList);
        CopyOnWriteArraySet current = new CopyOnWriteArraySet(currentChilds);
        current.removeAll(alreadyIn);
        alreadyIn.removeAll(currentChilds);
        doDelete(referenceBean,alreadyIn);
        doPut(referenceBean,path,current);
    }

    private void doPut(ReferenceBean referenceBean, String path, CopyOnWriteArraySet<String> current) throws Exception {
        for(String str:current){
            LocalExportBean localExportBean = JSON.parseObject(zkSupportKit.readData(path + "/" + str), LocalExportBean.class);
            if(referenceBean.getLocalExportAddrList()!=null)
                referenceBean.getLocalExportAddrList().add(str);
            else{
                CopyOnWriteArrayList<String> tmp = new CopyOnWriteArrayList<>();
                tmp.add(str);
                referenceBean.setLocalExportAddrList(tmp);
            }
            if(referenceBean.getLocalExportBeanList()!=null)
                referenceBean.getLocalExportBeanList().add(localExportBean);
            else{
                CopyOnWriteArrayList<LocalExportBean> tmp = new CopyOnWriteArrayList<>();
                tmp.add(localExportBean);
                referenceBean.setLocalExportBeanList(tmp);
            }
            ClientStarter clientStarter = ClientFactory.getClientStarter(localExportBean);
            clientStarter.connect(localExportBean,referenceBean.getClassName());
        }
    }

    private void doDelete(ReferenceBean referenceBean, CopyOnWriteArraySet<String> alreadyIn) throws IOException {
        CopyOnWriteArrayList<String> localExportAddrList = referenceBean.getLocalExportAddrList();
        CopyOnWriteArrayList<LocalExportBean> localExportBeanList = referenceBean.getLocalExportBeanList();
        CopyOnWriteArrayList<RemoteClient> remoteClients = referenceBean.getRemoteClients();
        for(String addr:alreadyIn){
            localExportAddrList.remove(addr);
            for(int i=0;i<localExportBeanList.size();i++){
                LocalExportBean localExportBean = localExportBeanList.get(i);
                if(addr.equals(localExportBean.getIp()+":"+localExportBean.getPort())){
                    localExportBeanList.remove(i);
                    break;
                }
            }
            for(int i=0;i<remoteClients.size();i++){
                RemoteClient client = remoteClients.get(i);
                if(addr.equals(client.getHostName()+":"+client.getPort())){
                    /** 务必要断开服务器连接！！！！！！！！！！！！ **/
                    ((Channel)client.getChannel()).close();
                    remoteClients.remove(i);
                    break;
                }
            }
        }
    }

    @Override
    public void connect(String url) {
        zkSupportKit = new ZKSupportKit(url,5000);
    }
}
