package com.qetch.springmvc.test.juc.shangguigu;

import java.util.concurrent.TimeUnit;
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

    public void sendSMS_sleep_4s() {
        lock.lock();
        try {
            TimeUnit.SECONDS.sleep(4);
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

    public void openPC() {
        System.out.println("***************openPC()");
    }
}

/**
 * 1    标准访问，请问先打印sms还是email
 * 2    sendSMS()睡眠4秒钟，请问先打印sms还是email
 * 3    新增普通方法openPC()，请问先打印sms还是email
 * 4    有两部手机，请问先打印sms还是email
 */
public class Lock_8 {

    public static void main(String[] args) {
        Phone phone = new Phone();
        Phone phone2 = new Phone();

        //test1(phone);

        //test2(phone);

        //test3(phone);

        //test4(phone, phone2);

    }

    private static void test4(Phone phone, Phone phone2) {
        new Thread(() -> {
            phone.sendSMS_sleep_4s();
        }, "AA").start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone2.sendEMail();
        }, "BB").start();

        /**
         * 运行结果：
         * ***************sendEMail()
         * （等待4秒）
         * ***************sendSMS()
         */
    }

    private static void test3(Phone phone) {
        new Thread(() -> {
            phone.sendSMS_sleep_4s();
        }, "AA").start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone.openPC();
        }, "BB").start();

        /**
         * 运行结果：
         * ***************openPC()
         * （等待4秒）
         * ***************sendSMS()
         */
    }

    private static void test2(Phone phone) {
        new Thread(() -> {
            phone.sendSMS_sleep_4s();
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
         * （等待4秒）
         * ***************sendSMS()
         * ***************sendEMail()
         */
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
