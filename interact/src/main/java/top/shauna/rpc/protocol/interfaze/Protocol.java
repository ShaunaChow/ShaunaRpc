package top.shauna.rpc.protocol.interfaze;

import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/29 9:40
 * @E-Mail z1023778132@icloud.com
 */
public interface Protocol {

    void getProtocolData(Object obj, ByteBuf out);

    void getProtocolObj(ByteBuf buf, List<Object> out);
}
