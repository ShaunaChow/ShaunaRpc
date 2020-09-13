package top.shauna.rpc.bean;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class MessageBean<T> {
    private Lock lock;
    private Condition condition;
    private T msg;

    public MessageBean() {
    }

    public MessageBean(Lock lock, Condition condition, T msg) {
        this.lock = lock;
        this.condition = condition;
        this.msg = msg;
    }

    public Condition getCondition() {

        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }
}
