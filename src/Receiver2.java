import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @author Wang Huadong
 * @author Robert Chen
 */
public class Receiver2
{
    public static void main(String[] args)
    {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageConsumer consumer;

        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://localhost:61616");
        try
        {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("Robert Chen");
            consumer = session.createConsumer(destination);

            while (true)
            {
                TextMessage message = (TextMessage) consumer.receive(4000);
                if (null != message)
                {
                    System.out.println("收到消息：" + message.getText());
                }
                else
                {
                    break;
                }
            }
        }
        catch (JMSException e)
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
}