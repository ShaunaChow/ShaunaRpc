package top.shauna.rpc.common.interfaces;

import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.ReferenceBean;

import java.util.List;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public interface Founder {
    void listen(ReferenceBean referenceBean);
    void found(ReferenceBean referenceBean) throws Exception;
    void connect(String url);
    List<LocalExportBean> getLocalExportBeans(Class interfaze);
}
