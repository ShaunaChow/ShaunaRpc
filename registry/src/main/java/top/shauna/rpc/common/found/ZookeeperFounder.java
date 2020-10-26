package top.shauna.rpc.common.found;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.ReferenceBean;
import top.shauna.rpc.bean.RemoteClient;
import top.shauna.rpc.client.ClientFactory;
import top.shauna.rpc.common.interfaces.Founder;
import top.shauna.rpc.interfaces.ClientStarter;
import top.shauna.rpc.supports.ZKSupportKit;
import top.shauna.rpc.util.CommonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */
@Slf4j
public class ZookeeperFounder implements Founder {
    private ZKSupportKit zkSupportKit;
    private static ConcurrentHashMap<String,ReferenceBean> connecterHolder = new ConcurrentHashMap<>();

    @Override
    public void listen(ReferenceBean referenceBean) {
        connecterHolder.put(referenceBean.getClassName(),referenceBean);
        zkSupportKit.subscribeChildChanges(CommonUtil.getZookeeperPath(referenceBean.getInterfaze()), (parentPath, currentChilds) -> doChange(parentPath,currentChilds));
    }

    /**
     * @Param: path 约定为/shauna/className/providers
     * **/
    @Override
    public void found(ReferenceBean referenceBean) throws Exception {
        Class interfaze = referenceBean.getInterfaze();
        String path = CommonUtil.getZookeeperPath(interfaze);
        CopyOnWriteArraySet providers = new CopyOnWriteArraySet(zkSupportKit.readChildren(path));
        if(providers.size()==0) {
            log.info("服务 "+interfaze+" 未找到提供者");
            return;
        }
        doPut(referenceBean,path, providers);
    }

    @Override
    public List<LocalExportBean> getLocalExportBeans(Class interfaze) {
        String path = CommonUtil.getZookeeperPath(interfaze);
        List<String> children = zkSupportKit.readChildren(path);
        List<LocalExportBean> res = new ArrayList<>();
        for (String localExportBeanString : children) {
            LocalExportBean localExportBean = JSON.parseObject(zkSupportKit.readData(path + "/" + localExportBeanString), LocalExportBean.class);
            res.add(localExportBean);
        }
        return res;
    }

    private void doChange(String path, List<String> currentChilds) throws Exception {
        if(!path.startsWith("/shauna/")) throw new Exception("发现功能监听路径错误 [doChange]");
        String className = path.substring(8);
        className = className.substring(0,className.indexOf("/"));

        ReferenceBean referenceBean = connecterHolder.get(className);

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
            clientStarter.connect(localExportBean,referenceBean);
            if(referenceBean.getRemoteClients().size()==0){
                int count = 10;
                while(referenceBean.getRemoteClients().size()==0&&count>0){
                    TimeUnit.MILLISECONDS.sleep(1000);
                }
                if(count==0) {
                    log.error("连接第一个服务提供者（" +
                            localExportBean.getProtocol() + "://" +
                            localExportBean.getIp() + ":" +
                            localExportBean.getPort() + "）失败！");
                    throw new Exception("连接第一个服务提供者（" +
                            localExportBean.getProtocol() + "://" +
                            localExportBean.getIp() + ":" +
                            localExportBean.getPort() + "）失败！");
                }
            }
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
                    client.getChannel().close();
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
