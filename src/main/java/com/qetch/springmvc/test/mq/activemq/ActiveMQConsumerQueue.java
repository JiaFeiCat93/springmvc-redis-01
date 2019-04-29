package com.qetch.springmvc.test.mq.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class ActiveMQConsumerQueue {

    private static final String BROKER_URL = "tcp://192.168.1.5:61616";
    
    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        try {
            // 1.创建工厂连接对象，需要指定ip和端口号
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            // 2.使用连接工厂创建一个连接对象
            connection = factory.createConnection();
            // 3.开启连接
            connection.start();
            // 4.使用连接对象创建一个session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 5.使用session创建一个queue
            Queue queue = session.createQueue("test-queue");
            // 6.使用session创建一个消费者
            consumer = session.createConsumer(queue);
            // 7.向consumer对象中设置一个messageListener对象，用来接收消息
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        try {
                            System.out.println(textMessage.getText());
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            // 8.程序等待接收用户消息
            System.in.read();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 9.关闭资源
                if (null != consumer) {
                    consumer.close();
                }
                if (null != session) {
                    session.close();
                }
                if (null != connection) {
                    connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
