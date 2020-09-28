package top.shauna.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/28 22:17
 * @E-Mail z1023778132@icloud.com
 */
public class ShaunaRPCEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        
    }
}
