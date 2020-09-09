package top.shauna.rpc.interfaces;

import top.shauna.rpc.bean.LocalExportBean;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public interface LocalExporter {
    public void init(LocalExportBean localExportBean) throws Exception;

    public void destory();
}
