package top.shauna.rpc.test.services.impl;

import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.test.services.Hello;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/14 15:01
 * @E-Mail z1023778132@icloud.com
 */
public class HelloCat implements Hello {
    @Override
    public String hello(String s) {
        return "helloCat "+s;
    }

    @Override
    public LocalExportBean hello(LocalExportBean bean) {
        return new LocalExportBean("sophia",520,"shauna");
    }

    @Override
    public void ok(int i) {
        System.out.println(i+100000);
    }

    @Override
    public int ok(int i, int j) {
        return i*100+j*100;
    }

    @Override
    public char okk(int i) {
        return (char)i;
    }


}
