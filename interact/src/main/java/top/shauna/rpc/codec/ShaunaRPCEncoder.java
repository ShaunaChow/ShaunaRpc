package top.shauna.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import top.shauna.rpc.protocol.ShaunaProtocol;
import top.shauna.rpc.protocol.interfaze.Protocol;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/28 22:17
 * @E-Mail z1023778132@icloud.com
 */
@Slf4j
public class ShaunaRPCEncoder extends MessageToByteEncoder {
    private Protocol protocol;

    public ShaunaRPCEncoder(){
        protocol = new ShaunaProtocol();
    }

    public ShaunaRPCEncoder(Protocol protocol){
        this.protocol = protocol;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        protocol.getProtocolData(msg,out);
    }
}
