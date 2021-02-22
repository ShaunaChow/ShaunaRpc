package top.shauna.rpc.interfaces;

import top.shauna.rpc.bean.InvokeInfo;
import top.shauna.rpc.bean.RemoteClient;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public interface LoadBalance {
    RemoteClient getRemoteClient(CopyOnWriteArrayList<RemoteClient> list, InvokeInfo invokeInfo);
}
