package top.shauna.rpc.common.interfaces;

import top.shauna.rpc.bean.ReferenceBean;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public interface Founder {
    public void listen(ReferenceBean referenceBean);
    public void found(ReferenceBean referenceBean) throws Exception;
    public void connect(String url);
}
