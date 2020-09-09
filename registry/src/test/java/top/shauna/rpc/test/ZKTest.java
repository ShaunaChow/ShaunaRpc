package top.shauna.rpc.test;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.junit.Test;
import top.shauna.rpc.supports.ZKSupportKit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class ZKTest {

    @Test
    public void test1(){
        ZKSupportKit zkSupportKit = new ZKSupportKit("39.105.89.185");
        zkSupportKit.subscribeDataChanges("/shauna", new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("changes:"+dataPath+" "+data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("deleted:"+dataPath);
            }
        });

        zkSupportKit.subscribeChildChanges("/shauna/top.shauna.rpc.test.Hello/providers", new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("child change:"+parentPath);
                for(String s:currentChilds) System.out.println(s);
            }
        });

        try {
            TimeUnit.DAYS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){
        String path = "/shauna/top.shauna.rpc.test.Hello/providers/127.0.0.1:8008";
        String className = path.substring(8);
        className = className.substring(0,className.indexOf("/"));
        System.out.println(className);

        System.out.println(UUID.randomUUID());
    }
}
