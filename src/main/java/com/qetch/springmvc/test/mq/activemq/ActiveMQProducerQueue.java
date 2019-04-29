package com.qetch.springmvc.test.mq.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ActiveMQProducerQueue {

    private static final String BROKER_URL = "tcp://192.168.1.5:61616";

    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            // 1.创建工厂连接对象，需要指定ip和端口号
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            // 2.使用连接工厂创建一个连接对象
            connection = factory.createConnection();
            // 3.开启连接
            connection.start();
            // 4.使用连接对象创建session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 5.使用session创建queue
            Queue queue = session.createQueue("test-queue");
            // 6.使用session创建生产者对象
            producer = session.createProducer(queue);
            // 7.使用session创建一个消息对象
            TextMessage textMessage = session.createTextMessage("hello test-queue");
            // 8.生产者发送消息
            producer.send(textMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭资源
                if (null != producer) {
                    producer.close();
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
