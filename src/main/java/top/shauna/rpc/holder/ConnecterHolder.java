package top.shauna.rpc.holder;

import top.shauna.rpc.bean.ReferenceBean;

import java.util.concurrent.ConcurrentHashMap;

public class ConnecterHolder {
    private static ConcurrentHashMap<String,ReferenceBean> connecterHolder = new ConcurrentHashMap<>();

    public static void put(String interfaze,ReferenceBean referenceBean){
        connecterHolder.put(interfaze,referenceBean);
    }

    public static ReferenceBean get(String interfaze){
        return connecterHolder.get(interfaze);
    }

    public static boolean contains(String interfaze){
        return connecterHolder.containsKey(interfaze);
    }
}
