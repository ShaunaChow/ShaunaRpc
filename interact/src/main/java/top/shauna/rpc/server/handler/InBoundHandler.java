package top.shauna.rpc.server.handler;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
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

        ByteBuf buf = (ByteBuf) msg;
        String request = buf.toString(CharsetUtil.UTF_8);

        RequestBeanWrapper requestBeanWrapper = JSON.parseObject(request, RequestBeanWrapper.class);

        ResponseBean response = MethodParser.getMethodParser().getResponse(requestBeanWrapper.getRequestBean());

        byte[] uuid = requestBeanWrapper.getUuid().getBytes();
        byte len = (byte)uuid.length;

        if(response.getRes() instanceof byte[]){
            byte[] res = (byte[])response.getRes();
            byte[] toBeSend = new byte[1024];
            toBeSend[0] = len;
            toBeSend[1] = 1;
            System.arraycopy(uuid,0,toBeSend,2,len);
            System.arraycopy(res,0,toBeSend,2+len,res.length);
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer(toBeSend));
        }else{
            ResponseBeanWrapper responseBeanWrapper = new ResponseBeanWrapper(requestBeanWrapper.getUuid(), response);
            String res = JSON.toJSONString(responseBeanWrapper);
            ctx.channel().write(Unpooled.copiedBuffer(new byte[]{len,0}));
            ctx.channel().write(Unpooled.copiedBuffer(uuid));
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer(res,CharsetUtil.UTF_8));
        }

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
