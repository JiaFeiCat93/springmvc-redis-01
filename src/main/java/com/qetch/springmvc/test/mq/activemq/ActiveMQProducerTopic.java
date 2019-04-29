package com.qetch.springmvc.test.mq.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ActiveMQProducerTopic {

    private static final String BROKER_URL = "tcp://192.168.1.5:61616";

    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            // 1.创建工厂连接对象，需要指定ip和端口号
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            // 2.使用连接工厂对象创建一个连接对象
            connection = factory.createConnection();
            // 3.开启连接
            connection.start();
            // 4.使用连接对象创建一个session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 5.使用session创建topic
            Topic topic = session.createTopic("test-topic");
            // 6.使用session创建一个生产者
            producer = session.createProducer(topic);
            // 7.使用session创建一个消息对象
            TextMessage textMessage = session.createTextMessage("hello test-topic");
            // 8.发送消息
            producer.send(textMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                // 9.关闭资源
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
