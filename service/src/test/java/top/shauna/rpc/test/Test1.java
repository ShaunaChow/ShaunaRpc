package top.shauna.rpc.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;
import top.shauna.rpc.bean.FoundBean;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.RegisterBean;
import top.shauna.rpc.bean.RemoteClient;
import top.shauna.rpc.config.PubConfig;
import top.shauna.rpc.interfaces.LoadBalance;
import top.shauna.rpc.protocol.serializer.HessianSerializer;
import top.shauna.rpc.service.ShaunaRPCHandler;
import top.shauna.rpc.test.impl.HelloImpl;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/25 15:27
 * @E-Mail z1023778132@icloud.com
 */
public class Test1 {

    @Test
    public void test1() throws IOException {
        LocalExportBean localExportBean = new LocalExportBean();
        localExportBean.setProtocol("netty");
        localExportBean.setIp("127.0.0.1");
        localExportBean.setPort(8090);

        PubConfig pubConfig = PubConfig.getInstance();
        if (pubConfig.getRegisterBean()==null) {
            RegisterBean registerBean = new RegisterBean("zookeeper","106.13.44.177:2181",null);
            pubConfig.setRegisterBean(registerBean);
        }
        if (pubConfig.getFoundBean()==null) {
            RegisterBean registerBean = pubConfig.getRegisterBean();
            FoundBean foundBean = new FoundBean(
                    registerBean.getPotocol(),
                    registerBean.getUrl(),
                    registerBean.getLoc()
            );
            pubConfig.setFoundBean(foundBean);
        }

        ShaunaRPCHandler.publishServiceBean(Hello.class, new HelloImpl(),localExportBean);

        System.in.read();
    }

    @Test
    public void test2() throws Exception {
        PubConfig pubConfig = PubConfig.getInstance();
        pubConfig.setTimeout(10000000L);
        if (pubConfig.getRegisterBean()==null) {
            RegisterBean registerBean = new RegisterBean("zookeeper","106.13.44.177:2181",null);
            pubConfig.setRegisterBean(registerBean);
        }
        if (pubConfig.getFoundBean()==null) {
            RegisterBean registerBean = pubConfig.getRegisterBean();
            FoundBean foundBean = new FoundBean(
                    registerBean.getPotocol(),
                    registerBean.getUrl(),
                    registerBean.getLoc()
            );
            pubConfig.setFoundBean(foundBean);
        }
        LocalExportBean localExportBean = new LocalExportBean();
        localExportBean.setProtocol("netty");
        localExportBean.setIp("127.0.0.1");
        localExportBean.setPort(8090);

        Hello hello = ShaunaRPCHandler.getReferenceProxy(Hello.class);
        Hello hello2 = ShaunaRPCHandler.getReferenceProxy(Hello.class);
        Hello hello3 = ShaunaRPCHandler.getReferenceProxy(Hello.class);

        new Thread(()->{
            while(true) {
                try {
                long t1 = System.currentTimeMillis();
                byte[] bytes = hello.okkk();
                long t2 = System.currentTimeMillis();
                System.out.println("客户端1接收到：" + bytes.length + "字节数据");
                System.out.println("RPC调用传输数据花费：" + (t2 - t1) + "毫秒");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(()->{
            while(true) {
                try {
                long t1 = System.currentTimeMillis();
                byte[] bytes = hello2.okkk();
                long t2 = System.currentTimeMillis();
                System.out.println("客户端2接收到：" + bytes.length + "字节数据");
                System.out.println("RPC调用传输数据花费：" + (t2 - t1) + "毫秒");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(()->{
            while(true) {
                try {
                    long t1 = System.currentTimeMillis();
                    byte[] bytes = hello3.okkk();
                    long t2 = System.currentTimeMillis();
                    System.out.println("客户端3接收到：" + bytes.length + "字节数据");
                    System.out.println("RPC调用传输数据花费：" + (t2 - t1) + "毫秒");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (true);
    }

    @Test
    public void test3() throws IOException {
        HessianSerializer hessianSerializer = new HessianSerializer();
        Bean bean = new Bean();
        SubBean subBean = new SubBean();
        subBean.setOkkk("okkkkkkkkkkkkk");
        bean.setAge(22);
        bean.setName("Shauna");
        bean.setSubBean(subBean);

        byte[] data = hessianSerializer.getData(22);

        int obj = (int)hessianSerializer.getObj(data);
        System.out.println(obj);

        long time = System.currentTimeMillis();

        ByteBuf out = Unpooled.copiedBuffer(new byte[100]);
        System.out.println("start1 "+out.readerIndex());
        out.readInt();
        System.out.println("start2 "+out.readerIndex());
        System.out.println("index "+out.writerIndex());
        int save = out.writerIndex();
        out.writerIndex(save+16);
        System.out.println("index "+out.writerIndex());
    }

    @Test
    public void test4() throws IOException {
        LocalExportBean localExportBean = new LocalExportBean();
        localExportBean.setProtocol("netty");
        localExportBean.setIp("127.0.0.1");
        localExportBean.setPort(9001);

        PubConfig pubConfig = PubConfig.getInstance();
        if (pubConfig.getRegisterBean()==null) {
            RegisterBean registerBean = new RegisterBean("zookeeper","8.131.57.153:2181",null);
            pubConfig.setRegisterBean(registerBean);
        }
        if (pubConfig.getFoundBean()==null) {
            RegisterBean registerBean = pubConfig.getRegisterBean();
            FoundBean foundBean = new FoundBean(
                    registerBean.getPotocol(),
                    registerBean.getUrl(),
                    registerBean.getLoc()
            );
            pubConfig.setFoundBean(foundBean);
        }

        ShaunaRPCHandler.publishServiceBean(Hello.class, new HelloImpl(),localExportBean);

        System.in.read();
    }

    @Test
    public void test5() throws Exception {
        LocalExportBean localExportBean = new LocalExportBean();
        localExportBean.setProtocol("netty");
        localExportBean.setIp("127.0.0.1");
        localExportBean.setPort(9009);
        PubConfig.getInstance().setTimeout(10000L);
        PubConfig.getInstance().setLoadbalance("top.shauna.rpc.loadbalance.LeastActiveLoadBalance");
        PubConfig.getInstance().setFoundBean(new FoundBean("zookeeper","8.131.57.153:2181",null));

        Hello hello = ShaunaRPCHandler.getReferenceProxy(Hello.class);

        while(true) {
            long t1 = System.currentTimeMillis();
            byte[] bytes = hello.okkk();
            long t2 = System.currentTimeMillis();
            System.out.println("客户端接收到：" + bytes.length + "字节数据");
            System.out.println("RPC调用传输数据花费：" + (t2 - t1) + "毫秒");
            Thread.sleep(3000);
        }
    }

    @Test
    public void test6() throws Exception {
        Object[] args = {};
        List<Object> objects = Arrays.asList(args);
        for (Object obj:objects) System.out.println(obj.hashCode()%Integer.MAX_VALUE);

        Set<Object> set = new HashSet<>();
        set.add("nihsad");
        set.add(new Date());
        set.add(new Bean());
        for(Object o: set){
            System.out.println(o+"  "+o.hashCode());
        }
    }
}

