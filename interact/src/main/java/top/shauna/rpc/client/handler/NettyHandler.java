package top.shauna.rpc.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.RemoteClient;
import top.shauna.rpc.client.channel.NettyChannel;
import top.shauna.rpc.holder.ConnecterHolder;

public class NettyHandler extends ChannelInboundHandlerAdapter {

    private String interfaze;
    private LocalExportBean localExportBean;

    public NettyHandler(String interfaze, LocalExportBean localExportBean) {
        super();
        this.interfaze = interfaze;
        this.localExportBean = localExportBean;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        NettyChannel nettyChannel = new NettyChannel(channel);
        RemoteClient client = new RemoteClient(localExportBean.getIp(), localExportBean.getPort(), nettyChannel, 0, 0.0);
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
