package top.shauna.rpc.client.handler;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import top.shauna.rpc.bean.*;
import top.shauna.rpc.client.channel.NettyChannel;
import top.shauna.rpc.client.responseholder.NettyMessageHolder;
import top.shauna.rpc.holder.ConnecterHolder;
import top.shauna.rpc.type.ResponseEnum;

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
        while(!channel.isWritable());
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
        byte[] array = new byte[buf.readableBytes()];
        buf.getBytes(0,array);
        if(array.length<2) return;
        byte len = array[0];
        byte type = array[1];
        String uuid = new String(array,2,len);

        if(type==0) {
            String response = new String(array,len+2,array.length-2-len);

            ResponseBeanWrapper responseBeanWrapper = JSON.parseObject(response, ResponseBeanWrapper.class);
            MessageBean messageBean = NettyMessageHolder.getMessage(uuid);
            messageBean.setMsg(responseBeanWrapper.getResponseBean());

            Lock lock = messageBean.getLock();
            lock.lock();
            try {
                messageBean.getCondition().signalAll();
            } finally {
                lock.unlock();
            }
        }else if(type==1){  /**定义为byte[]大数组！！！！用于传输大数据量！**/
            byte[] res = new byte[array.length-2-len];
            System.arraycopy(array,2+len,res,0,res.length);
            MessageBean messageBean = NettyMessageHolder.getMessage(uuid);
            ResponseBean responseBean = new ResponseBean(ResponseEnum.SUCCESS,res);
            messageBean.setMsg(responseBean);
            Lock lock = messageBean.getLock();
            lock.lock();
            try {
                messageBean.getCondition().signalAll();
            } finally {
                lock.unlock();
            }
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
