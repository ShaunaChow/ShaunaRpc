package top.shauna.rpc.server.handler;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import top.shauna.rpc.bean.RequestBean;
import top.shauna.rpc.bean.ResponseBean;
import top.shauna.rpc.server.parser.MethodParser;


public class InBoundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        String request = buf.toString(CharsetUtil.UTF_8);
        RequestBean responseBean = JSON.parseObject(request, RequestBean.class);

        ResponseBean response = MethodParser.getMethodParser().getResponse(responseBean);

        String res = JSON.toJSONString(response);

        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(res,CharsetUtil.UTF_8));

        super.channelRead(ctx, msg);
    }
}
