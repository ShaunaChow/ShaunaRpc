package top.shauna.rpc.server.handler;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.shauna.rpc.bean.RequestBean;
import top.shauna.rpc.bean.ResponseBean;
import top.shauna.rpc.server.parser.MethodParser;


public class InBoundHandler extends ChannelInboundHandlerAdapter {
    private static Logger log = LogManager.getLogger(InBoundHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        String request = buf.toString(CharsetUtil.UTF_8);
        System.out.println(request);
        RequestBean responseBean = JSON.parseObject(request, RequestBean.class);

        ResponseBean response = MethodParser.getMethodParser().getResponse(responseBean);

        String res = JSON.toJSONString(response);

        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(res,CharsetUtil.UTF_8));

        super.channelRead(ctx, msg);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelUnregistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("发生错误！"+cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }
}
