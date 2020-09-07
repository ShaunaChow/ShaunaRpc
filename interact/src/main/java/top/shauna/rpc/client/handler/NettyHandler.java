package top.shauna.rpc.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.shauna.rpc.bean.RemoteClient;
import top.shauna.rpc.client.connecter.holder.ConnecterHolder;

public class NettyHandler extends ChannelInboundHandlerAdapter {

    private String interfaze;

    public NettyHandler() {
        super();
    }

    public NettyHandler(String interfaze) {
        super();
        this.interfaze = interfaze;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        RemoteClient client = new RemoteClient(interfaze, channel, 0, 0.0);
        if(ConnecterHolder.contains(interfaze)){
            ConnecterHolder.get(interfaze).getRemoteClients().add(client);
        }else{
            throw new Exception("没有初始化ReferenceBean!");
        }
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
