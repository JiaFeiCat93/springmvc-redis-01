package com.qetch.springmvc.test.juc.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Test1 {

    public static void main(String[] args) throws InterruptedException {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        Thread thread = new Thread(() -> {
            lock.writeLock().lock();
            System.out.println("Thread real execute");
            lock.writeLock().unlock();
        });

        lock.writeLock().lock();
        lock.writeLock().lock();

        thread.start();

        Thread.sleep(200);

        System.out.println("release one lock");
        lock.writeLock().unlock();

        //System.out.println("release two lock");
        //lock.writeLock().unlock();
    }
}
