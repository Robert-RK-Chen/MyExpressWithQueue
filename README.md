# My Express With Queue

My Express With Queue, the second experiment of middleware

    一个基于ActiveMQ的物流动态信息发送/接受程序，配合MySQL5作为消息持久化的数据库

## 一、实验目的及要求
1. 掌握消息中间件基础知识
2. 掌握JMS的消息模型Queue和Topic
3. 熟悉JMS API
4. 熟悉ActiveMQ
5. 独立完成基于消息中间件ActiveMQ的物流信息模拟

## 二、程序运行环境及说明
    - JDK Version : JDK 16

    - 为了能够更好地进行消息持久化的存储，本程序采用了 MySQL 5.7 作为消息的持久化存储方式，因此您需要更改 ActiveMQ 环境下的配置文件来达到效果

## 三、配置文件的修改
在原配置文件中，消息的持久化存储默认使用 KahaDB
```xml
<persistenceAdapter>
    <kahaDB directory="${activemq.data}/kahadb"/>
</persistenceAdapter>
```
若要使用 MySQL 5.7 作为持久化的存储方式，请将上述的配置替换为
```xml
<persistenceAdapter>
    <jdbcPersistenceAdapter dataDirectory="${activemq.base}/data" dataSource="#mysql-ds"/>
</persistenceAdapter>
```
接下来，在`</broker>`标签结束后，请添加一个`bean`标签，内容如下：
```xml
<bean id="mysql-ds" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://localhost:3306/activemq?relaxAutoCommit=true&amp;useSSL=false"/>
    <property name="username" value="yourDbAdminName"/>
    <property name="password" value="yourDbPassword"/>
    <property name="poolPreparedStatements" value="true"/>
</bean>
```
请注意：
1. 在配置数据库连接时，请将 MySQL 安装目录下的`mysql-connector-java-version-bin.jar`拷贝到`~\apache-activemq-version\lib`下
    > 你也可以访问https://dev.mysql.com/downloads/connector/j/ 进行连接组件的下载
2. 请注意 ActiveMQ 安装目录`~\apache-activemq-version\lib\optional`下的`commons-dbcp`组件版本，`class="org.apache.commons.dbcp2.
   BasicDataSource"`需要做同步修改
   > 请参见：https://activemq.apache.org/jdbc-support
3. src 文件夹下，有两个 Receiver， 其中 Receiver 作为异步接收消息的消费者（使用监听器）， Receiver2 作为同步接收消息的消费者（使用轮询方式）