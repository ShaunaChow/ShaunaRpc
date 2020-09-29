package top.shauna.rpc.test;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/29 9:57
 * @E-Mail z1023778132@icloud.com
 */
@Setter
@Getter
@ToString
public class Bean implements Serializable {
    private String name;
    private Integer age;
    private SubBean subBean;
}
