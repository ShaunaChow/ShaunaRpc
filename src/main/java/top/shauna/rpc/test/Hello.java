package top.shauna.rpc.test;

import top.shauna.rpc.bean.LocalExportBean;

public interface Hello {
    public LocalExportBean helloCat(String name, LocalExportBean b);
    public void helloDog(int i);
}
