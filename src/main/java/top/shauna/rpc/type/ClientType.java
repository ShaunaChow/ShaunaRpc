package top.shauna.rpc.type;

import io.netty.channel.Channel;

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
