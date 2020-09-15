package top.shauna.rpc.client.channel;

import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import top.shauna.rpc.interfaces.Channel;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class NettyChannel implements Channel {
    private io.netty.channel.Channel channel;

    public NettyChannel(io.netty.channel.Channel channel){
        this.channel = channel;
    }

    @Override
    public void write(String msg) throws Exception {
        channel.writeAndFlush(Unpooled.copiedBuffer(msg,CharsetUtil.UTF_8));
    }

    @Override
    public void close() {
        channel.close();
    }
}
