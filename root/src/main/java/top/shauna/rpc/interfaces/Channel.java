package top.shauna.rpc.interfaces;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public interface Channel {

    void write(String msg) throws Exception;

    boolean isOk();

    void close();
}
