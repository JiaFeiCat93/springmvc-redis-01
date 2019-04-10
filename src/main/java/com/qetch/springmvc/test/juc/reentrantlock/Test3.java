package com.qetch.springmvc.test.juc.reentrantlock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 2.ReentrantReadWriteLock与synchronized的效率对比
 */
public class Test3 {

    public static void main(String[] args) {
        // 使用synchronized关键字
        //testSynchronized();

        // 使用ReentrantReadWriteLock
        testReentrantReadWriteLock();
    }

    private static void testSynchronized() {
        new Thread(() -> {
            get1(Thread.currentThread());
        }, "AA").start();

        new Thread(() -> {
            get1(Thread.currentThread());
        }, "BB").start();

        /**
         * 运行结果：
         * start time:1554909143594
         * AA:正在进行读操作......
         * AA:正在进行读操作......
         * AA:正在进行读操作......
         * AA:正在进行读操作......
         * AA:正在进行读操作......
         * AA:读操作完毕！
         * end time:1554909143698
         * start time:1554909143698
         * BB:正在进行读操作......
         * BB:正在进行读操作......
         * BB:正在进行读操作......
         * BB:正在进行读操作......
         * BB:正在进行读操作......
         * BB:读操作完毕！
         * end time:1554909143803
         *
         * 从运行结果可以看出，两个线程的读操作是顺序执行的，整个过程大概耗时200ms。
         */
    }

    public synchronized static void get1(Thread thread) {
        System.out.println("start time:" + System.currentTimeMillis());
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(thread.getName() + ":正在进行读操作......");
        }
        System.out.println(thread.getName() + ":读操作完毕！");
        System.out.println("end time:" + System.currentTimeMillis());
    }

    private static void testReentrantReadWriteLock() {
        new Thread(() -> {
            get2(Thread.currentThread());
        }, "AA").start();

        new Thread(() -> {
            get2(Thread.currentThread());
        }, "BB").start();

        /**
         * 运行结果：
         * start time:1554909650464
         * start time:1554909650464
         * BB:正在进行读操作......
         * AA:正在进行读操作......
         * BB:正在进行读操作......
         * AA:正在进行读操作......
         * AA:正在进行读操作......
         * BB:正在进行读操作......
         * BB:正在进行读操作......
         * AA:正在进行读操作......
         * AA:正在进行读操作......
         * BB:正在进行读操作......
         * BB:读操作完毕！
         * AA:读操作完毕！
         * end time:1554909650568
         * end time:1554909650568
         *
         * 从运行结果可以看出，两个线程的读操作是同时执行的，整个过程大概耗时100ms。
         * 通过两次实验的对比，我们可以看出来，ReentrantReadWriteLock的效率明显
         * 高于synchronized关键字。
         */
    }

    public static void get2(Thread thread) {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        lock.readLock().lock();
        System.out.println("start time:" + System.currentTimeMillis());
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(thread.getName() + ":正在进行读操作......");
        }
        System.out.println(thread.getName() + ":读操作完毕！");
        System.out.println("end time:" + System.currentTimeMillis());
        lock.readLock().unlock();
    }
}
