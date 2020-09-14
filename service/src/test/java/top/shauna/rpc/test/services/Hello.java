package top.shauna.rpc.test.services;

import top.shauna.rpc.bean.LocalExportBean;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/14 15:00
 * @E-Mail z1023778132@icloud.com
 */
public interface Hello {

    String hello(String s);

    LocalExportBean hello(LocalExportBean bean);

    void ok(int i);

    int ok(int i, int j);

    char okk(int i);
}
