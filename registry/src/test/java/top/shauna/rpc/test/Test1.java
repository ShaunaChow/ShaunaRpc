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
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.RegisterBean;
import top.shauna.rpc.bean.ServiceBean;
import top.shauna.rpc.common.factory.RegistryFactory;
import top.shauna.rpc.common.interfaces.Register;
import top.shauna.rpc.config.PubConfig;
import top.shauna.rpc.server.ExportFactory;
import top.shauna.rpc.interfaces.LocalExporter;
import top.shauna.rpc.supports.ZKSupportKit;
import top.shauna.rpc.type.ResponseEnum;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class Test1 {

    @Test
    public void test1() throws Exception {
        ServiceBean<?> bean = new ServiceBean<>();
        bean.setInterfaze(Hello.class);
        bean.setInterfaceImpl(null);
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
        bean.setPubConfig(config);

        Register register = RegistryFactory.getRegister(bean);
        register.doRegist(bean);
        LocalExporter exporter = ExportFactory.getExporter(bean);
        exporter.init(localExportBean);

        ZKSupportKit zkSupportKit = new ZKSupportKit("39.105.89.185");
        System.out.println(zkSupportKit.readData("/shauna/top.shauna.rpc.test.Hello/helloCat/providers/39.105.89.185:8008"));

        Class<?> aClass = Class.forName("top.shauna.rpc.bean.LocalExportBean");
        System.out.println(aClass.getName());
        System.out.println(aClass.getMethod("getProtocal").getName());
        LocalExportBean o = (LocalExportBean) aClass.newInstance();
        System.out.println(o);

    }

    @Test
    public void test2() throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
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
            ChannelFuture sync = serverBootstrap.bind(8008).sync();
            sync.channel().closeFuture().sync();
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    class MyHandler extends ChannelInboundHandlerAdapter{
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
    public void test3() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new MyHandler1());
                    }
                });
        ChannelFuture sync = bootstrap.connect("127.0.0.1", 8008).sync();
        sync.channel().closeFuture().sync();
    }

    class MyHandler1 extends ChannelInboundHandlerAdapter{
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            Channel channel = ctx.channel();
            channel.writeAndFlush(Unpooled.copiedBuffer("你好",CharsetUtil.UTF_8));
            System.out.println("客户端ok");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            System.out.println(buf.toString(CharsetUtil.UTF_8));
        }
    }

    class Out1 extends ChannelOutboundHandlerAdapter{
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

    public String okk(int b, String a){
        return "1";
    }
}
