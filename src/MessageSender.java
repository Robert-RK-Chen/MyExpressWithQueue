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
         * connectionFactory    ���ӹ�����JMS ������������
         * connection           JMS �ͻ��˵� JMS Provider ������
         * session              һ�����ͻ������Ϣ���߳�
         * destination          ��Ϣ��Ŀ�ģ���Ϣ���͸�˭
         * producer             ��Ϣ������
         * brokerUrl            ������Ϣ�ĵ�ַ���������������ڵĽ����߷�����Ϣ�����öԷ��ṩ��IP��ַ
         * queueName            ��Ϣ���е�����
         * */
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageProducer producer;
        String brokerUrl = "tcp://localhost:61616";
        String queueName = "Robert Chen";

        // ���� ConnectionFactory ʵ�����󣬴˴����� ActiveMQ ��ʵ�� jar
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
            // �Ƿ�֧���������Ϊ true�������Եڶ�������
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            // ��ȡ session ע�����ֵ���� ActiveMQ �� console ����
            destination = session.createQueue(queueName);
            // �õ���Ϣ�����ߡ������ߡ�
            producer = session.createProducer(destination);
            // ���ó־û�
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            // ������Ϣ���߷�����ȡ
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
                "�����ѷ���",
                "���Ϻ��С�����ѵ���Ӫҵ��",
                "���Ϻ��С��������ת�˵����Ϻ���ɢ���ġ�",
                "���Ϻ��С�����ڡ��Ϻ���ɢ���ġ�׼���������Ϻ����ż�ɢ���ġ�",
                "���Ϻ��С�����ڡ��Ϻ����ż�ɢ���ġ�׼�����������зֲ����ġ�",
                "���人�С�����ѵ�����зֲ����ġ�",
                "���人�С�����ڡ����зֲ����ġ�׼���������人���Ӫҵ�㡿",
                "���人�С�����ѵ���人���Ӫҵ�㡿",
                "���人�С�������������У��뱣�ֵ绰��ͨ",
                "���人�С�����ѱ�ǩ�գ���¼APP�ɲ鿴ǩ������Ϣ"};

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss  ");
        Date date = new Date(System.currentTimeMillis());

        /* ���´���Ϊ�ֶ�������Ϣ
         * Scanner scanner = new Scanner(System.in);
         * System.out.print("��������Ϣ���ݣ�");
         * String text = scanner.nextLine();
         * */
        for (String text : messages)
        {
            try
            {
                TextMessage message = session.createTextMessage(formatter.format(date) + text);
                producer.send(message);
                System.out.println("��Ϣ�ѳɹ����ͣ�");
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
                System.out.println("��Ϣ����ʧ�ܣ����� " + e.getMessage());
            }
        }
    }
}