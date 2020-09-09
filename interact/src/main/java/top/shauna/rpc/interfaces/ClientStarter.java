package top.shauna.rpc.interfaces;

import top.shauna.rpc.bean.LocalExportBean;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public interface ClientStarter {

    public void connect(LocalExportBean localExportBean,String interfaze) throws Exception;
}
