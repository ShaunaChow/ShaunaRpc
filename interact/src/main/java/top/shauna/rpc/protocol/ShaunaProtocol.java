package top.shauna.rpc.protocol;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import top.shauna.rpc.bean.RequestBeanWrapper;
import top.shauna.rpc.bean.ResponseBeanWrapper;
import top.shauna.rpc.protocol.interfaze.Protocol;
import top.shauna.rpc.protocol.interfaze.Serializer;
import top.shauna.rpc.protocol.serializer.HessianSerializer;

import java.util.List;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/29 9:30
 * @E-Mail z1023778132@icloud.com
 */
@Slf4j
public class ShaunaProtocol implements Protocol {
    private Serializer serializer;
    private static Integer HEADER_LENGTH = 16;
    private static byte[] MAGIC_CODE = {62,01};
    private static byte RESPONSE_FLAG = (byte)(1<<7);
    private static byte REQUEST_FLAG = (byte)(0<<7);
    private static int BASIC_LENGTH = 16;

    public ShaunaProtocol(){
        serializer = new HessianSerializer();
    }

    public ShaunaProtocol(Serializer serializer){
        this.serializer = serializer;
    }

    @Override
    public void getProtocolData(Object obj, ByteBuf out) {
        if(obj instanceof ResponseBeanWrapper){
            encodeResponse((ResponseBeanWrapper)obj, out);
        }else if(obj instanceof RequestBeanWrapper){
            encodeRequest((RequestBeanWrapper)obj, out);
        }else{
            log.error("占不支持的数据类型！！！");
            return;
        }
    }

    private void encodeRequest(RequestBeanWrapper request, ByteBuf out) {
        /** 设置协议头 **/
        byte[] header = new byte[HEADER_LENGTH];
        /** 设置魔数 **/
        copyByte(header,0,MAGIC_CODE,0,2);
        /** 设置请求/回复标志位 **/
        header[2] |= REQUEST_FLAG;
        /** 设置ID **/
        setHeaderID(header,4, request.getId());

        byte[] body = serializer.getData(request);
        int length = body.length;
        setLength(header,12,length);

        /** 检测数据长度待定 **/

        out.writeBytes(header);
        out.writeBytes(body);
    }

    private void encodeResponse(ResponseBeanWrapper response, ByteBuf out) {
        /** 设置协议头 **/
        byte[] header = new byte[HEADER_LENGTH];
        /** 设置魔数 **/
        copyByte(header,0,MAGIC_CODE,0,2);
        /** 设置请求/回复标志位 **/
        header[2] |= RESPONSE_FLAG;
        /** 设置ID **/
        setHeaderID(header,4,response.getId());

        byte[] body = serializer.getData(response);
        int length = body.length;
        setLength(header,12,length);

        /** 检测数据长度待定 **/

        out.writeBytes(header);
        out.writeBytes(body);
    }

    private void setLength(byte[] header, int offset, int length) {
        header[offset+0] = (byte) (length>>>24);
        header[offset+1] = (byte) (length>>>16);
        header[offset+2] = (byte) (length>>>8);
        header[offset+3] = (byte) (length>>>0);
    }

    private void setHeaderID(byte[] header, int offset, long id) {
        header[offset+0] = (byte) (id>>>56);
        header[offset+1] = (byte) (id>>>48);
        header[offset+2] = (byte) (id>>>40);
        header[offset+3] = (byte) (id>>>32);
        header[offset+4] = (byte) (id>>>24);
        header[offset+5] = (byte) (id>>>16);
        header[offset+6] = (byte) (id>>>8);
        header[offset+7] = (byte) id;
    }

    private void copyByte(byte[] des, int desoffset, byte[] src, int srcoffset, int len) {
        System.arraycopy(src,srcoffset,des,desoffset,len);
    }

    @Override
    public void getProtocolObj(ByteBuf buf, List<Object> out) {
        if (buf.readableBytes()<BASIC_LENGTH) {
            log.warn("数据包长度太短！！！");
            return ;
        }
        int beginIndex = buf.readerIndex();
        byte[] header = new byte[HEADER_LENGTH];
        buf.readBytes(header);
        if(header[0]!=MAGIC_CODE[0]||header[1]!=MAGIC_CODE[1]) {
            log.warn("接收到无用的数据包！！！");
            return;
        }
        buf.readerIndex(beginIndex+12);
        int len = buf.readInt();
        if(buf.readableBytes()<len){
            buf.readerIndex(beginIndex);
            return;
        }
        byte[] body = new byte[len];
        buf.readBytes(body);
        Object obj = serializer.getObj(body);
        out.add(obj);
        buf.discardReadBytes();
    }
}
