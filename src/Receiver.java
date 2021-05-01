import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author Wang Huadong
 * @author Robert Chen
 */
public class Receiver
{
    public static void main(String[] args)
    {
        /*
         * connectionFactory      连接工厂，JMS 用它创建连接
         * connection             JMS 客户端到 JMS Provider 的连接
         * session                一个发送或接收消息的线程
         * destination            消息的目的地，消息发送给谁
         * consumer               消费者，消息接收者
         * brokerUrl              消息队列的地址，如果想接受局域网内的消息，请让消息发送者提供其IP地址
         * queueName            消息队列的名称
         * */
        ConnectionFactory connectionFactory;
        Connection connection;
        Session session;
        Destination destination;
        MessageConsumer consumer;
        String brokerUrl = "tcp://localhost:61616";
        String queueName = "Robert Chen";

        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                brokerUrl);
        try
        {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动连接
            connection.start();
            // 获取操作连接
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 获取 session 注意参数值在 ActiveMQ 的 console 配置
            destination = session.createQueue(queueName);
            // 创建消费者
            consumer = session.createConsumer(destination);
            // 异步接收消息
            consumer.setMessageListener(message ->
            {
                TextMessage textMessage = (TextMessage) message;
                try
                {
                    System.out.println("收到消息：" + textMessage.getText());
                    textMessage.acknowledge();
                }
                catch (JMSException e)
                {
                    e.printStackTrace();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}