package top.shauna.rpc.common.interfaces;

public interface Register {

    public boolean isValid(String addr);

    public void doRegist(String url,boolean isProvider);

    public void connect(String url);
}
