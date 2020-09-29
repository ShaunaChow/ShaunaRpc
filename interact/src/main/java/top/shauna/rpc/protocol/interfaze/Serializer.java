package top.shauna.rpc.protocol.interfaze;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/29 9:46
 * @E-Mail z1023778132@icloud.com
 */
public interface Serializer {

    byte[] getData(Object obj);

    Object getObj(byte[] data);
}
