package top.shauna.rpc.interfaces;

import top.shauna.rpc.bean.LocalExportBean;

public interface ClientStarter {

    public void connect(LocalExportBean localExportBean,String interfaze) throws Exception;
}
