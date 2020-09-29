package top.shauna.rpc.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.shauna.rpc.bean.RequestBeanWrapper;
import top.shauna.rpc.bean.ResponseBean;
import top.shauna.rpc.bean.ResponseBeanWrapper;
import top.shauna.rpc.server.parser.MethodParser;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class InBoundHandler extends ChannelInboundHandlerAdapter {
    private static Logger log = LogManager.getLogger(InBoundHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RequestBeanWrapper requestBeanWrapper = (RequestBeanWrapper) msg;

        ResponseBean response = MethodParser.getMethodParser().getResponse(requestBeanWrapper.getRequestBean());

        ResponseBeanWrapper responseBeanWrapper = new ResponseBeanWrapper(requestBeanWrapper.getId(), response);

        ctx.channel().writeAndFlush(responseBeanWrapper);

        super.channelRead(ctx, msg);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("发生错误！"+cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }
}
