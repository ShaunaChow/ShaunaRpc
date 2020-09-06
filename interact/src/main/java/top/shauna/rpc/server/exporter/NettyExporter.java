package top.shauna.rpc.server.exporter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.server.handler.InBoundHandler;
import top.shauna.rpc.interfaces.LocalExporter;


public class NettyExporter implements LocalExporter {

    @Override
    public void init(LocalExportBean localExportBean) throws Exception {
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
                            socketChannel.pipeline().addLast("handler1",new InBoundHandler());
                        }
                    });

            ChannelFuture sync = serverBootstrap.bind(localExportBean.getPort()).sync();
            sync.channel().closeFuture().sync();
        }catch (Exception e){
            throw new Exception("本地暴露失败, "+e.getMessage());
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}
