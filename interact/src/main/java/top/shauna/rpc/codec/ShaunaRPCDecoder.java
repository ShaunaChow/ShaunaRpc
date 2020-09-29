package top.shauna.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import top.shauna.rpc.protocol.ShaunaProtocol;
import top.shauna.rpc.protocol.interfaze.Protocol;

import java.util.List;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/29 16:57
 * @E-Mail z1023778132@icloud.com
 */
@Slf4j
public class ShaunaRPCDecoder extends ByteToMessageDecoder {
    private Protocol protocol;

    public ShaunaRPCDecoder(){
        protocol = new ShaunaProtocol();
    }

    public ShaunaRPCDecoder(Protocol protocol){
        this.protocol = protocol;
    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        protocol.getProtocolObj(buf,out);
    }
}
