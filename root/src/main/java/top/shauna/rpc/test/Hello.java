package top.shauna.rpc.test;

import top.shauna.rpc.bean.LocalExportBean;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */
public interface Hello {
    public LocalExportBean helloCat(String name, LocalExportBean b);
    public void helloDog(int i);
    public byte[] okkk();
}
