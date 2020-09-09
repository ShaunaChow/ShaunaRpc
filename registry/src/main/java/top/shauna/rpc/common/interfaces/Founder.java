package top.shauna.rpc.common.interfaces;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public interface Founder {
    public void listen(String path);
    public void found(String path) throws Exception;
    public void connect(String url);
}
