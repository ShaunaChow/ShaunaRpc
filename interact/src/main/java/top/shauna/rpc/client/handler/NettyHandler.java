package top.shauna.rpc.client.handler;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.MessageBean;
import top.shauna.rpc.bean.RemoteClient;
import top.shauna.rpc.bean.ResponseBeanWrapper;
import top.shauna.rpc.client.channel.NettyChannel;
import top.shauna.rpc.client.responseholder.NettyMessageHolder;
import top.shauna.rpc.holder.ConnecterHolder;

import java.util.concurrent.locks.Lock;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        String response = buf.toString(CharsetUtil.UTF_8);

        ResponseBeanWrapper responseBeanWrapper = JSON.parseObject(response, ResponseBeanWrapper.class);
        String uuid = responseBeanWrapper.getUuid();
        MessageBean messageBean = NettyMessageHolder.getMessage(uuid);
        messageBean.setMsg(responseBeanWrapper.getResponseBean());

        Lock lock = messageBean.getLock();
        lock.lock();
        try {
            messageBean.getCondition().signalAll();
        }finally {
            lock.unlock();
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
