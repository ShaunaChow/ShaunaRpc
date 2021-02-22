package top.shauna.rpc.bean;

import java.util.List;

/**
 * @Author Shauna.Chow
 * @Date 2021/2/22 14:25
 * @E-Mail z1023778132@icloud.com
 */
public class InvokeInfo {
    private Long stamp;
    private List<Object> args;

    public Long getStamp() {
        return stamp;
    }

    @Override
    public int hashCode() {
        long hashCode = stamp.hashCode();
        for(Object obj:args){
            hashCode += obj.hashCode();
        }
        return (int)(hashCode%Integer.MAX_VALUE);
    }

    public void setStamp(Long stamp) {
        this.stamp = stamp;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }

    public InvokeInfo() {

    }

    public InvokeInfo(Long stamp, List<Object> args) {

        this.stamp = stamp;
        this.args = args;
    }
}
