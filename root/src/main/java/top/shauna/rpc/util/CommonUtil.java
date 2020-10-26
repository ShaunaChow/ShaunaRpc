package top.shauna.rpc.util;

/**
 * @Author Shauna.Chou
 * @Date 2020/10/26 20:01
 * @E-Mail z1023778132@icloud.com
 */
public class CommonUtil {
    public static String getZookeeperPath(Class interfaze) {
        return "/shauna/"+interfaze.getName()+"/providers";
    }
}
