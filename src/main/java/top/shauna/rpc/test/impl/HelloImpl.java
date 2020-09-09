package top.shauna.rpc.test.impl;

import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.test.Hello;

public class HelloImpl implements Hello {
    @Override
    public LocalExportBean helloCat(String name, LocalExportBean b) {
        return new LocalExportBean("okkkkkkk", 9898, String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void helloDog(int i) {
        System.out.println("helloDag "+i);
    }
}
