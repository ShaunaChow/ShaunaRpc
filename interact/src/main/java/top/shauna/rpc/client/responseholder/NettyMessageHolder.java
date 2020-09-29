package top.shauna.rpc.client.responseholder;

import top.shauna.rpc.bean.MessageBean;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class NettyMessageHolder {
    private static ConcurrentHashMap<Long,MessageBean> map = new ConcurrentHashMap<>();

    public static void put(Long uuid, MessageBean messageBean){
        map.put(uuid,messageBean);
    }

    public static MessageBean getMessage(Long uuid){
        return map.get(uuid);
    }

    public static void remote(Long uuid){
        map.remove(uuid);
    }
}
