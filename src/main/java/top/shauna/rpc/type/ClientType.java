package top.shauna.rpc.type;

import io.netty.channel.Channel;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public enum ClientType {
    Netty(Channel.class);

    private Class T;

    public Class getT() {
        return T;
    }

    public void setT(Class t) {
        T = t;
    }

    ClientType(Class t) {

        T = t;
    }
}
