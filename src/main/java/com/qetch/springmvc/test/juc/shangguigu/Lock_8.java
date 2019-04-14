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

    public synchronized void sendSMS_sleep_4s_2() {
        try {
            TimeUnit.SECONDS.sleep(4);
            System.out.println("***************sendSMS()");
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    public synchronized static void sendSMS_sleep_4s_static_2() {
        try {
            TimeUnit.SECONDS.sleep(4);
            System.out.println("***************sendSMS()");
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    public synchronized void sendEMail_2() {
        System.out.println("***************sendEMail()");
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

    public synchronized static void sendEMail_static_2() {
        System.out.println("***************sendEMail()");
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
 *
 * 笔记：
 * 一个对象里面如果有多个synchronized方法，某一个时刻内，只要一个线程去调用其中的一个synchronized方法了，
 * 其他的线程都只能等待，换句话说，某一时刻内，只能有唯一一个线程去访问这些synchronized方法
 *
 * 锁的是当前对象this，被锁定后，其它的线程都不能进入到当前对象的其它的synchronized方法
 *
 * 加个普通方法后，发现和同步锁无关
 * 换成两个对象后，不是同一把锁了，情况立刻变化
 *
 * 都换成静态同步方法后，情况又变化
 * 所有的非静态同步方法用的都是同一把锁--实例对象本身
 *
 * 也就是说，如果一个实例对象的非静态同步方法获取锁后，该实例对象的其它非静态同步方法必须等待获取锁的方法释放后，才能获取锁，
 * 可是别的实例对象的非静态同步方法因为跟该实例对象的非静态同步方法用的是不同的锁，
 * 所以无需等待该实例对象已获取锁的非静态同步方法释放锁就可以获取它们自己的锁。
 *
 * 所有的静态同步方法用的也是同一把锁--类对象本身，
 * 这两把锁是两个不同的对象，所以静态同步方法与非静态同步方法之间是不会有竟态条件的。
 * 但是一旦一个静态同步方法获取锁后，其他的静态同步方法都必须等待该方法释放锁后，才能获取锁，而不管是同一个实例对象的静态同步方法之间，
 * 还是不同实例对象的静态同步方之间，只要它们是同一个类的实例对象。
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

        test8(phone, phone2);
    }

    private static void test8(Phone phone, Phone phone2) {
        new Thread(() -> {
            phone.sendSMS_sleep_4s_static_2();
        }, "AA").start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone2.sendEMail_2();
        }, "BB").start();

        /**
         * 运行结果：
         * ***************sendEMail()
         * （等待4秒）
         * ***************sendSMS()
         */
    }

    private static void test7(Phone phone) {
        new Thread(() -> {
            phone.sendSMS_sleep_4s_static_2();
        }, "AA").start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone.sendEMail_2();
        }, "BB").start();

        /**
         * 运行结果：
         * ***************sendEMail()
         * （等待4秒）
         * ***************sendSMS()
         */
    }

    private static void test6(Phone phone, Phone phone2) {
        new Thread(() -> {
            phone.sendSMS_sleep_4s_static_2();
        }, "AA").start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone2.sendEMail_static_2();
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
            phone.sendSMS_sleep_4s_static_2();
        }, "AA").start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone.sendEMail_static_2();
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
            phone.sendSMS_sleep_4s_2();
        }, "AA").start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone2.sendEMail_2();
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
