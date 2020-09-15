package top.shauna.rpc.server.exporter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.common.ShaunaThreadPool;
import top.shauna.rpc.server.handler.InBoundHandler;
import top.shauna.rpc.interfaces.LocalExporter;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class NettyExporter implements LocalExporter {
    private NioEventLoopGroup boss;
    private NioEventLoopGroup work;

    @Override
    public void init(LocalExportBean localExportBean) throws Exception {
        if (localExportBean.getExported()!=null&&localExportBean.getExported()==true) {
            return;
        }
        boss = new NioEventLoopGroup(1);
        work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("handler1",new InBoundHandler());
                        }
                    });
            /** 此处必须等待建立连接！防止服务注册后第一时间连接的客户端连接不上 **/
            ChannelFuture sync = serverBootstrap.bind(localExportBean.getPort()).sync();
            while (!sync.isDone()) ;
            /** 此处必须开起一个线程监听服务器状态，必要时刻shutdownGracefully **/
            ShaunaThreadPool.threadPool.execute(()->{
                try {
                    sync.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    boss.shutdownGracefully();
                    work.shutdownGracefully();
                }
            });
            localExportBean.setExported(true);
        }catch (Exception e){
            throw new Exception("本地暴露失败, "+e.getMessage());
        }
    }

    @Override
    public void destory() {
        if(boss!=null)
            boss.shutdownGracefully();
        if(work!=null)
            work.shutdownGracefully();
    }
}
