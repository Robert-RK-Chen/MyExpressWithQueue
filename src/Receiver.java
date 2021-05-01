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
         * connectionFactory      ���ӹ�����JMS ������������
         * connection             JMS �ͻ��˵� JMS Provider ������
         * session                һ�����ͻ������Ϣ���߳�
         * destination            ��Ϣ��Ŀ�ĵأ���Ϣ���͸�˭
         * consumer               �����ߣ���Ϣ������
         * brokerUrl              ��Ϣ���еĵ�ַ���������ܾ������ڵ���Ϣ��������Ϣ�������ṩ��IP��ַ
         * queueName            ��Ϣ���е�����
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
            // ����ӹ����õ����Ӷ���
            connection = connectionFactory.createConnection();
            // ��������
            connection.start();
            // ��ȡ��������
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // ��ȡ session ע�����ֵ�� ActiveMQ �� console ����
            destination = session.createQueue(queueName);
            // ����������
            consumer = session.createConsumer(destination);
            // �첽������Ϣ
            consumer.setMessageListener(message ->
            {
                TextMessage textMessage = (TextMessage) message;
                try
                {
                    System.out.println("�յ���Ϣ��" + textMessage.getText());
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