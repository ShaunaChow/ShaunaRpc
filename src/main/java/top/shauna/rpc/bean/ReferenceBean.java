package top.shauna.rpc.bean;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ReferenceBean {
    private String className;
    private Class<?> interfaze;
    private List<LocalExportBean> localExportBeanList;
    private CopyOnWriteArrayList<RemoteClient> remoteClients;

    public ReferenceBean(String className, Class<?> interfaze, List<LocalExportBean> localExportBeanList, CopyOnWriteArrayList<RemoteClient> remoteClients) {
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

    public List<LocalExportBean> getLocalExportBeanList() {
        return localExportBeanList;
    }

    public void setLocalExportBeanList(List<LocalExportBean> localExportBeanList) {
        this.localExportBeanList = localExportBeanList;
    }

    public CopyOnWriteArrayList<RemoteClient> getRemoteClients() {
        return remoteClients;
    }

    public void setRemoteClients(CopyOnWriteArrayList<RemoteClient> remoteClients) {
        this.remoteClients = remoteClients;
    }
}
