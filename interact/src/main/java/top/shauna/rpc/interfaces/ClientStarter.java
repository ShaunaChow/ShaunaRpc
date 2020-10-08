package top.shauna.rpc.interfaces;

import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.bean.ReferenceBean;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public interface ClientStarter {

    void connect(LocalExportBean localExportBean,ReferenceBean referenceBean) throws Exception;
}
