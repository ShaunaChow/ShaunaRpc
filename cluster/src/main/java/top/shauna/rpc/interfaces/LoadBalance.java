package top.shauna.rpc.interfaces;

import top.shauna.rpc.bean.ReferenceBean;
import top.shauna.rpc.bean.RemoteClient;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public interface LoadBalance {
    public RemoteClient getRemoteClient(ReferenceBean referenceBean);
}
