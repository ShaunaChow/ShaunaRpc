package top.shauna.rpc.test.impl;

import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.test.Hello;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class HelloImpl implements Hello {
    @Override
    public LocalExportBean helloCat(String name, LocalExportBean b) {
        System.out.println("okkkkk");
        return new LocalExportBean("okkkkkkk", 9898, String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void helloDog(int i) {
        System.out.println("helloDag "+i);
    }

    @Override
    public byte[] okkk() {
        byte[] res = new byte[1024*1024*256];
        for(int i=0;i<res.length;i++) res[i] = (byte)i;
        System.out.println("okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
        return res;
    }
}
