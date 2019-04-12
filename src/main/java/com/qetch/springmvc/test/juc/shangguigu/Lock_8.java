package com.qetch.springmvc.test.juc.shangguigu;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class Phone {

    private static ReentrantLock lock = new ReentrantLock();

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

    public static void sendSMS_sleep_4s_static() {
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

    public static void sendEMail_static() {
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
 * 5    两个静态同步方法，有一部手机，请问先打印sms还是email
 * 6    两个静态同步方法，有两部手机，请问先打印sms还是email
 * 7    一个静态同步方法，一个普通同步方法，同一部手机，请问先打印sms还是email
 * 8    一个静态同步方法，一个普通同步方法，两部手机，请问先打印sms还是email
 */
public class Lock_8 {

    public static void main(String[] args) {
        Phone phone = new Phone();
        Phone phone2 = new Phone();

        //test1(phone);

        //test2(phone);

        //test3(phone);

        //test4(phone, phone2);

        //test5(phone);

        //test6(phone, phone2);

        //test7(phone);

        new Thread(() -> {
            phone.sendSMS_sleep_4s_static();
        }, "AA").start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone2.sendEMail();
        }, "BB").start();
    }

    private static void test7(Phone phone) {
        new Thread(() -> {
            phone.sendSMS_sleep_4s_static();
        }, "AA").start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone.sendEMail();
        }, "BB").start();
    }

    private static void test6(Phone phone, Phone phone2) {
        new Thread(() -> {
            phone.sendSMS_sleep_4s_static();
        }, "AA").start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone2.sendEMail_static();
        }, "BB").start();

        /**
         * 运行结果：
         * （等待4秒）
         * ***************sendSMS()
         * ***************sendEMail()
         */
    }

    private static void test5(Phone phone) {
        new Thread(() -> {
            phone.sendSMS_sleep_4s_static();
        }, "AA").start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone.sendEMail_static();
        }, "BB").start();

        /**
         * 运行结果：
         * （等待4秒）
         * ***************sendSMS()
         * ***************sendEMail()
         *
         */
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
