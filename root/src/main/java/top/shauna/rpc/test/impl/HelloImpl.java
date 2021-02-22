package top.shauna.rpc.test.impl;

import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.test.Hello;

import java.io.Serializable;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class HelloImpl implements Hello,Serializable {
    @Override
    public LocalExportBean helloCat(String name, LocalExportBean b) {
        return new LocalExportBean("ShaunaRPC", 9898, String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void helloDog(int i) {
        System.out.println("helloDag "+i);
    }

    @Override
    public byte[] okkk() {
        byte[] res = new byte[1024*1024*256];
        for(int i=0;i<res.length;i++) res[i] = (byte)i;
        System.out.println("服务端发送了："+1024*1024*256+"字节大数据");
        return res;
    }

    @Override
    public void testOKK(Hello hello) {
        System.out.println("okkkk");
        System.out.println(hello.toString());
    }

    @Override
    public String toString() {
        return "HelloImpl{}+++++++++++++++++++";
    }
}
