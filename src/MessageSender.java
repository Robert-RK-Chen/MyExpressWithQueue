import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static java.lang.Thread.sleep;

/**
 * @author Wang Huadong
 * @author Robert Chen
 */
public class MessageSender
{
    public static void main(String[] args)
    {
        /*
         * connectionFactory    连接工厂，JMS 用它创建连接
         * connection           JMS 客户端到 JMS Provider 的连接
         * session              一个发送或接收消息的线程
         * destination          消息的目的，消息发送给谁
         * producer             消息发送者
         * brokerUrl            发送消息的地址，如果想向局域网内的接收者发送消息，请让对方提供其IP地址
         * queueName            消息队列的名称
         * */
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageProducer producer;
        String brokerUrl = "tcp://localhost:61616";
        String queueName = "Robert Chen";

        // 构造 ConnectionFactory 实例对象，此处采用 ActiveMQ 的实现 jar
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
            // 是否支持事务，如果为 true，则会忽略第二个参数
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            // 获取 session 注意参数值须在 ActiveMQ 的 console 配置
            destination = session.createQueue(queueName);
            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            // 设置持久化
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            // 构造消息或者方法获取
            sendMessage(session, producer);
            session.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (null != connection)
                {
                    connection.close();
                }
            }
            catch (Throwable ignore)
            {
            }
        }
    }

    public static void sendMessage(Session session, MessageProducer producer)
    {
        String[] messages = new String[]{
                "卖家已发货",
                "【上海市】快件已到达营业点",
                "【上海市】快件正在转运到【上海集散中心】",
                "【上海市】快件在【上海集散中心】准备发往【上海虹桥集散中心】",
                "【上海市】快件在【上海虹桥集散中心】准备发往【华中分拨中心】",
                "【武汉市】快件已到达【华中分拨中心】",
                "【武汉市】快件在【华中分拨中心】准备发往【武汉马湖营业点】",
                "【武汉市】快件已到达【武汉马湖营业点】",
                "【武汉市】快件正在派送中，请保持电话畅通",
                "【武汉市】快递已被签收，登录APP可查看签收人信息"};

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss  ");
        Date date = new Date(System.currentTimeMillis());

        /* 以下代码为手动输入消息
         * Scanner scanner = new Scanner(System.in);
         * System.out.print("请输入消息内容：");
         * String text = scanner.nextLine();
         * */
        for (String text : messages)
        {
            try
            {
                TextMessage message = session.createTextMessage(formatter.format(date) + text);
                producer.send(message);
                System.out.println("消息已成功发送！");
                try
                {
                    sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            catch (JMSException e)
            {
                System.out.println("消息发送失败，请检查 " + e.getMessage());
            }
        }
    }
}