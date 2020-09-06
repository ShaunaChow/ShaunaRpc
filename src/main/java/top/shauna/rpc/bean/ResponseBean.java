package top.shauna.rpc.bean;

import top.shauna.rpc.type.ResponseEnum;

public class ResponseBean<R> {
    private ResponseEnum code;
    private R res;

    public ResponseBean(ResponseEnum code, R res) {
        this.code = code;
        this.res = res;
    }

    public ResponseBean() {
    }

    public ResponseEnum getCode() {
        return code;
    }

    public void setCode(ResponseEnum code) {
        this.code = code;
    }

    public R getRes() {
        return res;
    }

    public void setRes(R res) {
        this.res = res;
    }
}
