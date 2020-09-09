package top.shauna.rpc.interfaces;

import top.shauna.rpc.bean.ReferenceBean;
import top.shauna.rpc.bean.RemoteClient;

public interface LoadBalance {
    public RemoteClient getRemoteClient(ReferenceBean referenceBean);
}
