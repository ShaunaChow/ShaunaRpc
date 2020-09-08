package top.shauna.rpc.common.interfaces;

public interface Founder {
    public void listen(String path);
    public void found(String path) throws Exception;
    public void connect(String url);
}
