package top.shauna.rpc.test.impl;

import com.alibaba.fastjson.JSON;
import top.shauna.rpc.bean.LocalExportBean;
import top.shauna.rpc.test.Hello;

public class HelloImpl implements Hello {
    @Override
    public LocalExportBean helloCat(String name, LocalExportBean b) {
        System.out.println(name+" helloCat "+ JSON.toJSONString(b));
        return new LocalExportBean("okkkkkkk", 9898, "localhost");
    }

    @Override
    public void helloDog(int i) {
        System.out.println("helloDag "+i);
    }
}
