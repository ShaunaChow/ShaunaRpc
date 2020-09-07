package top.shauna.rpc.bean;

public class RemoteClient<T> {
    private String hostName;
    private Integer port;
    private T channel;
    private Integer invokeNums;
    private Double avgTimeMills;
    private Double factor;

    public RemoteClient() {
        factor = 0.4;
    }

    public RemoteClient(String hostName, Integer port, T channel, Integer invokeNums, Double avgTimeMills, Double factor) {
        this.hostName = hostName;
        this.port = port;
        this.channel = channel;
        this.invokeNums = invokeNums;
        this.avgTimeMills = avgTimeMills;
        this.factor = factor;
    }

    public RemoteClient(String hostName, Integer port, T channel, Integer invokeNums, Double avgTimeMills) {
        this();
        this.hostName = hostName;
        this.port = port;
        this.channel = channel;
        this.invokeNums = invokeNums;
        this.avgTimeMills = avgTimeMills;
    }

    public void updateAvgTime(long spend){
        if(invokeNums==0) avgTimeMills = (double)spend;
        else avgTimeMills = (1-factor)*avgTimeMills + factor*spend;
        invokeNums++;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public T getChannel() {
        return channel;
    }

    public void setChannel(T channel) {
        this.channel = channel;
    }

    public Integer getInvokeNums() {
        return invokeNums;
    }

    public void setInvokeNums(Integer invokeNums) {
        this.invokeNums = invokeNums;
    }

    public Double getAvgTimeMills() {
        return avgTimeMills;
    }

    public void setAvgTimeMills(Double avgTimeMills) {
        this.avgTimeMills = avgTimeMills;
    }

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
