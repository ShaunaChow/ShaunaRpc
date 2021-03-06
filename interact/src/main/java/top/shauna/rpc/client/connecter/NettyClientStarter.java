package top.shauna.rpc.client.connecter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.NoArgsConstructor;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.ReferenceBean;
import top.shauna.rpc.client.handler.NettyHandler;
import top.shauna.rpc.codec.ShaunaRPCDecoder;
import top.shauna.rpc.codec.ShaunaRPCEncoder;
import top.shauna.rpc.common.ShaunaThreadPool;
import top.shauna.rpc.interfaces.ClientStarter;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

@NoArgsConstructor
public class NettyClientStarter implements ClientStarter {
    @Override
    public void connect(LocalExportBean localExportBean, ReferenceBean referenceBean) {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("ShaunaEncoder",new ShaunaRPCEncoder());
                        socketChannel.pipeline().addLast("ShaunaDecoder",new ShaunaRPCDecoder());
                        socketChannel.pipeline().addLast(referenceBean.getClassName(), new NettyHandler(referenceBean,localExportBean));
                    }
                });

        try {
            ChannelFuture sync = bootstrap.connect(localExportBean.getIp(), localExportBean.getPort()).sync();
            while(!sync.isDone()) ;
            ShaunaThreadPool.threadPool.execute(() -> {
                try {
                    sync.channel().closeFuture().sync();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    group.shutdownGracefully();
                }
            });
        } catch (InterruptedException e) {
        e.printStackTrace();
    }
    }
}
