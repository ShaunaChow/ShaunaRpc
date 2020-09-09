package top.shauna.rpc.client.responseholder;

import top.shauna.rpc.bean.MessageBean;

import java.util.concurrent.ConcurrentHashMap;

public class NettyMessageHolder {
    private static ConcurrentHashMap<String,MessageBean> map = new ConcurrentHashMap<>();

    public static void put(String uuid, MessageBean messageBean){
        map.put(uuid,messageBean);
    }

    public static MessageBean getMessage(String uuid){
        return map.get(uuid);
    }

    public static void remote(String uuid){
        map.remove(uuid);
    }
}
