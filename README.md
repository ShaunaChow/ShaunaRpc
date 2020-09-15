# ShaunaRpc

远程调用框架，底层使用Netty作为通讯架构，并使用Zookeeper作为注测发现中心，这一切都是可选项，都可以用自己实现的插件来代替ShaunaRpc中的组件。


## 一个例子：

1.引入maven依赖：
<dependency>
    <groupId>top.shauna</groupId>
    <artifactId>service</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

2.编写Provider和consumer的配置文件
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:shauna="http://www.shauna.top/schema/shauna"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.shauna.top/schema/shauna
       http://www.shauna.top/schema/shauna/shauna.xsd">

    <shauna:pubconfig appname="okkk" threadnums="10"/>

    <shauna:register protocol="zookeeper" url="x.x.x.x:2181"/>

    <shauna:export protocol="netty" port="8081" ip="127.0.0.1"/>

    <shauna:service interface="top.shauna.example.interfaces.TeacherInfo" ref="top.shauna.example.provider.impl.TeacherInfoImpl"/>
</beans>
=============================================
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:shauna="http://www.shauna.top/schema/shauna"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.shauna.top/schema/shauna
       http://www.shauna.top/schema/shauna/shauna.xsd">

    <context:component-scan base-package="top.shauna.example.consumer"/>

    <shauna:pubconfig appname="okkk"/>

    <shauna:found protocol="zookeeper" url="x.x.x.x:2181"/>

    <shauna:reference interface="top.shauna.example.interfaces.TeacherInfo"/>
</beans>

3. 编写Provider和consumer启动类：
public class Provider {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
        System.in.read();
    }
}
==========================================
public class Consumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
        TeacherInfo teacherInfo = context.getBean(TeacherInfo.class);
        teacherInfo.listAll();
    }
}

4. 启动Provider,在启动consumer即可实现远程调用！
