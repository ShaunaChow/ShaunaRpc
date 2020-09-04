package top.shauna.rpc.common.interfaces;

import top.shauna.rpc.bean.ServiceBean;

public interface Register {

    public boolean isValid(String addr);

    public void doRegist(ServiceBean<?> serviceBean);

    public void connect(String url);
}
