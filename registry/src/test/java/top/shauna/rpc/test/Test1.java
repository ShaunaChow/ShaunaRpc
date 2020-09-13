package top.shauna.rpc.test;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.junit.Test;
import top.shauna.rpc.bean.*;
import top.shauna.rpc.client.ClientFactory;
import top.shauna.rpc.common.factory.FounderFactory;
import top.shauna.rpc.common.found.ZookeeperFounder;
import top.shauna.rpc.common.interfaces.Founder;
import top.shauna.rpc.holder.ConnecterHolder;
import top.shauna.rpc.common.ShaunaThreadPool;
import top.shauna.rpc.common.factory.RegistryFactory;
import top.shauna.rpc.common.interfaces.Register;
import top.shauna.rpc.config.PubConfig;
import top.shauna.rpc.holder.MethodsHolder;
import top.shauna.rpc.interfaces.ClientStarter;
import top.shauna.rpc.proxy.ReferenceProxyFactory;
import top.shauna.rpc.server.ExportFactory;
import top.shauna.rpc.interfaces.LocalExporter;
import top.shauna.rpc.test.impl.HelloImpl;
import top.shauna.rpc.type.ResponseEnum;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class Test1 {

    @Test
    public void test1() throws Exception {
        ServiceBean bean = new ServiceBean<>();
        MethodsHolder.putMethod(Hello.class.getTypeName(),bean);
        bean.setInterfaze(Hello.class);
        bean.setInterfaceImpl(new HelloImpl());
        LocalExportBean localExportBean = new LocalExportBean("netty",8009,"127.0.0.2");
        bean.setLocalExportBean(localExportBean);
        Map<String,Method> map = new HashMap<>();
        for (Method method : Hello.class.getMethods()) {
            map.put(method.getName(),method);
        }
        bean.setMethods(map);
        PubConfig config = PubConfig.getInstance();
        config.setApplicationName("testtttt");
        config.setRegisterBean(new RegisterBean("zookeeper","39.105.89.185:2181",null));

        LocalExporter exporter = ExportFactory.getExporter(bean);
        exporter.init(localExportBean);
        Register register = RegistryFactory.getRegister();
        register.doRegist(bean);

        Thread.sleep(5000);
//        ZKSupportKit zkSupportKit = new ZKSupportKit("39.105.89.185");
//        System.out.println(zkSupportKit.readData("/shauna/top.shauna.rpc.test.Hello/helloCat/providers/39.105.89.185:8008"));

        Class<?> aClass = Class.forName("top.shauna.rpc.bean.LocalExportBean");
        System.out.println(aClass.getName());
        System.out.println(aClass.getMethod("getProtocol").getName());
        LocalExportBean o = (LocalExportBean) aClass.newInstance();
        System.out.println(o);

        System.in.read();

    }

    @Test
    public void test2() throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new MyHandler("111"));
                        socketChannel.pipeline().addLast(new MyHandler("222"));
                        socketChannel.pipeline().addLast(new Out1());
                    }
                });

        ShaunaThreadPool.threadPool.execute(()->{
            try {
                ChannelFuture sync = serverBootstrap.bind(8008).sync();
                System.out.println("connect done");
                sync.channel().closeFuture().sync();
                System.out.println("closeFuture");
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                boss.shutdownGracefully();
                work.shutdownGracefully();
            }
        });
        System.out.println("done");
    }

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new MyHandler("111"));
                        socketChannel.pipeline().addLast(new MyHandler("222"));
                        socketChannel.pipeline().addLast(new Out1());
                    }
                });

        ShaunaThreadPool.threadPool.execute(()->{
            try {
                ChannelFuture sync = serverBootstrap.bind(8008).sync();
                System.out.println("connect done");
                sync.channel().closeFuture().sync();
                System.out.println("closeFuture");
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                boss.shutdownGracefully();
                work.shutdownGracefully();
            }
        });
        System.out.println("done");
    }

    static class MyHandler extends ChannelInboundHandlerAdapter{
        private String ss;
        public MyHandler(String s){
            ss = s;
        }
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println(ss);
            ctx.channel().write(Unpooled.copiedBuffer(ss,CharsetUtil.UTF_8));
            System.out.println(ss);
            ctx.fireChannelRead(msg);
        }
    }

    @Test
    public void test3() throws InterruptedException, ExecutionException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("nameeeeeeeeeeeeeeeeee",new MyHandler1());
                        socketChannel.pipeline().addLast(new Out1());
                    }
                });
        ShaunaThreadPool.threadPool.execute(()->{
            ChannelFuture sync = null;
            try {
                sync = bootstrap.connect("localhost", 8008).sync();
                sync.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                group.shutdownGracefully();
            }
        });
        System.out.println("okkkkkkkkkkkkkkkkkkkkkk");
        while(true){
            Thread.sleep(2000);
            if(ch!=null){
                System.out.println("ceshi111");
                ChannelFuture future = ch.writeAndFlush(Unpooled.copiedBuffer("测试1", CharsetUtil.UTF_8));
                System.out.println("ansss = "+future.get());
                System.out.println("ceshi222");
//                break;
            }
        }
    }
    private static volatile Channel ch = null;

    class MyHandler1 extends ChannelInboundHandlerAdapter{
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            Channel channel = ctx.channel();
            ch = channel;
            channel.writeAndFlush(Unpooled.copiedBuffer("你好",CharsetUtil.UTF_8));
            System.out.println("客户端ok");
            System.out.println(ctx.name());
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            System.out.println(buf.toString(CharsetUtil.UTF_8));
        }
    }

    static class Out1 extends ChannelOutboundHandlerAdapter{
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("out");
            ctx.write(msg);
            ctx.flush();
        }
    }

    @Test
    public void test4() throws NoSuchMethodException {
        String s = JSON.toJSONString(ResponseEnum.SUCCESS);
        System.out.println(s);
        ResponseEnum responseEnum = JSON.parseObject(s, ResponseEnum.class);
        System.out.println(responseEnum);
        Class<Test1> testClass = Test1.class;
        Method okk = testClass.getMethod("okk", int.class, String.class);
        for (Parameter parameter : okk.getParameters()) {
            System.out.println(parameter.getType());
        }

        double integer = JSON.parseObject("23", double.class);
        System.out.println(integer);
    }

    @Test
    public void test5() throws NoSuchMethodException {
        ServiceBean bean = new ServiceBean<>();
        bean.setInterfaze(Hello.class);
        bean.setInterfaceImpl(new HelloImpl());
        LocalExportBean localExportBean = new LocalExportBean("netty",8008,"39.105.89.185");
        bean.setLocalExportBean(localExportBean);
        Map<String,Method> map = new HashMap<>();
        for (Method method : Hello.class.getMethods()) {
            map.put(method.getName(),method);
        }
        bean.setMethods(map);
        PubConfig config = PubConfig.getInstance();
        config.setApplicationName("testtttt");
        config.setRegisterBean(new RegisterBean("zookeeper","39.105.89.185:2181",null));

//        Method method = null;
//        HelloImpl hello = new HelloImpl();
//        method = Hello.class.getMethod("helloCat",String.class,LocalExportBean.class);
//        List<String> values = Arrays.asList(JSON.toJSONString("shauna"),JSON.toJSONString(localExportBean));
        Method method = null;
        HelloImpl hello = new HelloImpl();
        method = Hello.class.getMethod("helloDog",int.class);
        List<String> values = Arrays.asList(JSON.toJSONString(500));

        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[values.size()];
        Object res = null;
        try {
            for (int i = 0; i < parameters.length; i++) {
                System.out.println(values.get(i)+"  "+parameters[i].getType());
                args[i] = JSON.parseObject(values.get(i), parameters[i].getType());
            }
            res = method.invoke(hello, args);
            System.out.println("ans = "+JSON.toJSONString(res));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test6() throws Exception {
        ServiceBean bean = new ServiceBean<>();
        bean.setInterfaze(Hello.class);
        bean.setInterfaceImpl(new HelloImpl());
        LocalExportBean localExportBean = new LocalExportBean("netty",8008,"127.0.0.1");
        bean.setLocalExportBean(localExportBean);
        Map<String,Method> map = new HashMap<>();
        for (Method method : Hello.class.getMethods()) {
            map.put(method.getName(),method);
        }
        bean.setMethods(map);
        PubConfig config = PubConfig.getInstance();
        config.setApplicationName("testtttt");
        config.setRegisterBean(new RegisterBean("zookeeper","39.105.89.185:2181",null));

        ReferenceBean referenceBean = new ReferenceBean();
        referenceBean.setRemoteClients(new CopyOnWriteArrayList<>());
        ConnecterHolder.put("abc",referenceBean);

        ClientStarter clientStarter = ClientFactory.getClientStarter(localExportBean);
        clientStarter.connect(localExportBean,"abc");

        ReferenceBean abc = ConnecterHolder.get("abc");
        while(abc.getRemoteClients().size()==0);
        Thread.sleep(5000);
        RemoteClient client = abc.getRemoteClients().get(0);
        Channel channel = (Channel) client.getChannel();

        channel.writeAndFlush(Unpooled.copiedBuffer("222222222458692222222222222222",CharsetUtil.UTF_8));
        channel.close();
        System.in.read();
    }

    public String okk(int b, String a){
        return "1";
    }

    @Test
    public void test7() throws Exception {
        ServiceBean bean = new ServiceBean<>();
        bean.setInterfaze(Hello.class);
        bean.setInterfaceImpl(new HelloImpl());
        LocalExportBean localExportBean = new LocalExportBean("netty", 8008, "127.0.0.1");
        bean.setLocalExportBean(localExportBean);
        Map<String, Method> map = new HashMap<>();
        for (Method method : Hello.class.getMethods()) {
            map.put(method.getName(), method);
        }
        bean.setMethods(map);
        PubConfig config = PubConfig.getInstance();
        config.setApplicationName("testtttt");
        config.setRegisterBean(new RegisterBean("zookeeper", "39.105.89.185:2181", null));

        ReferenceBean referenceBean = new ReferenceBean();
        referenceBean.setClassName("top.shauna.rpc.test.Hello");
        referenceBean.setRemoteClients(new CopyOnWriteArrayList<>());
        ConnecterHolder.put("top.shauna.rpc.test.Hello", referenceBean);

        ReferenceBean abc = ConnecterHolder.get("top.shauna.rpc.test.Hello");

        ZookeeperFounder zookeeperFounder = new ZookeeperFounder();
        zookeeperFounder.connect("39.105.89.185:2181");
        zookeeperFounder.listen(abc.getInterfaze());

        CopyOnWriteArrayList<RemoteClient> remoteClients = abc.getRemoteClients();

        while (true) {
            Thread.sleep(2000);
            for (RemoteClient remoteClient : remoteClients) {
                System.out.println(remoteClient.getHostName() + ":" + remoteClient.getPort());
            }
            System.out.println("=================================");
        }
    }


    @Test
    public void test8() throws Exception {
        ServiceBean bean = new ServiceBean<>();
        bean.setInterfaze(Hello.class);
        bean.setInterfaceImpl(new HelloImpl());
        LocalExportBean localExportBean = new LocalExportBean("netty", 8008, "127.0.0.1");
        bean.setLocalExportBean(localExportBean);
        Map<String, Method> map = new HashMap<>();
        for (Method method : Hello.class.getMethods()) {
            map.put(method.getName(), method);
        }
        bean.setMethods(map);
        PubConfig config = PubConfig.getInstance();
        config.setApplicationName("testtttt");
        config.setRegisterBean(new RegisterBean("zookeeper", "39.105.89.185:2181", null));
        config.setFoundBean(new FoundBean("zookeeper", "39.105.89.185:2181", null));

        ReferenceBean referenceBean = new ReferenceBean();
        referenceBean.setClassName("top.shauna.rpc.test.Hello");
        referenceBean.setRemoteClients(new CopyOnWriteArrayList<>());
        ConnecterHolder.put("top.shauna.rpc.test.Hello", referenceBean);

        ReferenceBean abc = ConnecterHolder.get("top.shauna.rpc.test.Hello");

        Founder founder = FounderFactory.getFounder();
        founder.found(abc.getInterfaze());

        founder.listen(abc.getInterfaze());

        CopyOnWriteArrayList<RemoteClient> remoteClients = abc.getRemoteClients();

        while (true) {
            Thread.sleep(2000);
            for (RemoteClient remoteClient : remoteClients) {
                System.out.println(remoteClient.getHostName() + ":" + remoteClient.getPort());
            }
            System.out.println("=================================");
        }
    }

    @Test
    public void test9() throws Exception {
        ServiceBean bean = new ServiceBean<>();
        bean.setInterfaze(Hello.class);
        bean.setInterfaceImpl(new HelloImpl());
        LocalExportBean localExportBean = new LocalExportBean("netty", 8008, "127.0.0.1");
        bean.setLocalExportBean(localExportBean);
        Map<String, Method> map = new HashMap<>();
        for (Method method : Hello.class.getMethods()) {
            map.put(method.getName(), method);
        }
        bean.setMethods(map);
        PubConfig config = PubConfig.getInstance();
        config.setApplicationName("testtttt");
        config.setRegisterBean(new RegisterBean("zookeeper", "39.105.89.185:2181", null));
        config.setFoundBean(new FoundBean("zookeeper", "39.105.89.185:2181", null));
        config.setTimeout(2000L);

        ReferenceBean referenceBean = new ReferenceBean();
        referenceBean.setClassName("top.shauna.rpc.test.Hello");
        referenceBean.setInterfaze(Hello.class);
        referenceBean.setRemoteClients(new CopyOnWriteArrayList<>());
        ConnecterHolder.put("top.shauna.rpc.test.Hello", referenceBean);

        ReferenceBean abc = ConnecterHolder.get("top.shauna.rpc.test.Hello");

        Founder founder = FounderFactory.getFounder();
        founder.found(abc.getInterfaze());

        founder.listen(abc.getInterfaze());

        CopyOnWriteArrayList<RemoteClient> remoteClients = abc.getRemoteClients();

        while (true) {
            Thread.sleep(2000);
            int i = 0;
            for (RemoteClient remoteClient : remoteClients) {
                System.out.println(remoteClient.getHostName() + ":" + remoteClient.getPort());
                i = 1;
            }
            System.out.println("=================================");
            if(i==1) break;
        }
        Hello proxy = ReferenceProxyFactory.getProxy(referenceBean);
        while(true) {
//            Thread.sleep(1000);
            System.out.println(proxy.helloCat("shauna", new LocalExportBean("shaunaaaaa", 2020, "haha")));
        }
    }
}
