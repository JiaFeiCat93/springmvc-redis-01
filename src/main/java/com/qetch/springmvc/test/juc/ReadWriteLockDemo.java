package com.qetch.springmvc.test.juc;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class MyQueue {
    private Object obj;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void writeObj(Object obj) {
        lock.writeLock().lock();
        try {
            this.obj = obj;
            System.out.println(Thread.currentThread().getName() + "\t" + obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void readObj() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t" + this.obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }
}

/**
 * 一个线程写入，100个线程读取
 */
public class ReadWriteLockDemo {

    public static void main(String[] args) throws InterruptedException {
        MyQueue myQueue = new MyQueue();
        new Thread(() -> {
            myQueue.writeObj("ClassName1018");
        }, "writeThread").start();

        Thread.sleep(100);

        for (int i = 1; i <= 100; i++) {
            new Thread(() -> {
                myQueue.readObj();
            }, String.valueOf(i)).start();
        }
    }
}
