package top.shauna.rpc.type;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public enum ResponseEnum {
    SUCCESS(200,"success"),
    MISSING_PARAMS(400,"missing_params"),
    NO_SUCH_METHOD(401,"no_such_method"),
    NO_SUCH_CLASS(402,"no_such_class"),
    PARAM_ERROR(403,"param_error"),
    TIME_OUT(404,"time_out");

    private int code;
    private String meg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMeg() {
        return meg;
    }

    public void setMeg(String meg) {
        this.meg = meg;
    }

    ResponseEnum(int code, String meg) {

        this.code = code;
        this.meg = meg;
    }

    @Override
    public String toString() {
        return "ResponseEnum{" +
                "code=" + code +
                ", meg='" + meg + '\'' +
                '}';
    }
}
