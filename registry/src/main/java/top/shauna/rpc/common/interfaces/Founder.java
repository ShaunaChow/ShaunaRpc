package top.shauna.rpc.common.interfaces;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public interface Founder {
    public void listen(Class interfaze);
    public void found(Class interfaze) throws Exception;
    public void connect(String url);
}
