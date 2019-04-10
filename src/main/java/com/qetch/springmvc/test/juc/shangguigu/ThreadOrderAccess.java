package com.qetch.springmvc.test.juc.shangguigu;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareResource {
    private int number = 1; // 1->AA 2->BB 3->CC
    private Lock lock = new ReentrantLock();
    Condition c1 = lock.newCondition();
    Condition c2 = lock.newCondition();
    Condition c3 = lock.newCondition();

    public void print5(int totalLoop) {
        lock.lock();
        try {
            while (number != 1) { // 当前不是AA线程，所以等待
                c1.await();
            }
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t totalLoop:" + totalLoop);
            }
            number = 2; // AA线程执行完毕后，立即修改标志位
            c2.signal(); // AA线程执行完毕后，只通知BB线程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print10(int totalLoop) {
        lock.lock();
        try {
            while (number != 2) { // 当前不是AA线程，所以等待
                c2.await();
            }
            for (int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t totalLoop:" + totalLoop);
            }
            number = 3; // AA线程执行完毕后，立即修改标志位
            c3.signal(); // AA线程执行完毕后，只通知BB线程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print15(int totalLoop) {
        lock.lock();
        try {
            while (number != 3) { // 当前不是AA线程，所以等待
                c3.await();
            }
            for (int i = 1; i <= 15; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t totalLoop:" + totalLoop);
            }
            number = 1; // AA线程执行完毕后，立即修改标志位
            c1.signal(); // AA线程执行完毕后，只通知BB线程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

/**
 * 多线程之间按顺序调用，实现A->B->C
 * 三个线程启动，要求如下：
 * AA打印5次，BB打印10次，CC打印15次
 * 接着
 * AA打印5次，BB打印10次，CC打印15次
 * ......来10轮
 */
public class ThreadOrderAccess {

    public static void main(String[] args) {
        ShareResource sr = new ShareResource();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                sr.print5(i);
            }
        }, "AA").start();
        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                sr.print10(i);
            }
        }, "BB").start();
        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                sr.print15(i);
            }
        }, "CC").start();
    }
}
