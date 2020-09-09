package top.shauna.rpc.common.interfaces;

import top.shauna.rpc.bean.ServiceBean;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public interface Register {

    public boolean isValid(String addr);

    public void doRegist(ServiceBean<?> serviceBean);

    public void connect(String url);
}
