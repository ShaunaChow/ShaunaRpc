package top.shauna.rpc.bean;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class ReferenceBean {
    private String className;
    private Class<?> interfaze;
    private CopyOnWriteArrayList<LocalExportBean> localExportBeanList;
    private CopyOnWriteArrayList<String> localExportAddrList;
    private CopyOnWriteArrayList<RemoteClient> remoteClients;

    public ReferenceBean(String className, Class<?> interfaze, CopyOnWriteArrayList<LocalExportBean> localExportBeanList, CopyOnWriteArrayList<RemoteClient> remoteClients) {
        this.className = className;
        this.interfaze = interfaze;
        this.localExportBeanList = localExportBeanList;
        this.remoteClients = remoteClients;
    }

    public ReferenceBean() {
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class<?> getInterfaze() {
        return interfaze;
    }

    public void setInterfaze(Class<?> interfaze) {
        this.interfaze = interfaze;
    }

    public CopyOnWriteArrayList<LocalExportBean> getLocalExportBeanList() {
        return localExportBeanList;
    }

    public void setLocalExportBeanList(CopyOnWriteArrayList<LocalExportBean> localExportBeanList) {
        this.localExportBeanList = localExportBeanList;
    }

    public CopyOnWriteArrayList<RemoteClient> getRemoteClients() {
        return remoteClients;
    }

    public void setRemoteClients(CopyOnWriteArrayList<RemoteClient> remoteClients) {
        this.remoteClients = remoteClients;
    }

    public CopyOnWriteArrayList<String> getLocalExportAddrList() {
        return localExportAddrList;
    }

    public void setLocalExportAddrList(CopyOnWriteArrayList<String> localExportAddrList) {
        this.localExportAddrList = localExportAddrList;
    }
}
