package com.qetch.springmvc.test.juc.shangguigu;

import java.util.concurrent.locks.ReentrantLock;

class Phone {

    private ReentrantLock lock = new ReentrantLock();

    public void sendSMS() {
        lock.lock();
        try {
            System.out.println("***************sendSMS()");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void sendEMail() {
        lock.lock();
        try {
            System.out.println("***************sendEMail()");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

/**
 * 1    标准访问，请问先打印sms还是email
 */
public class Lock_8 {

    public static void main(String[] args) {
        Phone phone = new Phone();

        test1(phone);
    }

    private static void test1(Phone phone) {
        new Thread(() -> {
            phone.sendSMS();
        }, "AA").start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone.sendEMail();
        }, "BB").start();

        /**
         * 运行结果：
         * ***************sendSMS()
         * ***************sendEMail()
         *
         * 结论：
         */
    }
}
