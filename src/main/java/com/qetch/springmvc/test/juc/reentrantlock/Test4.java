package com.qetch.springmvc.test.juc.reentrantlock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 3.1ReentrantReadWriteLock读写锁关系
 *
 * 3.2ReentrantReadWriteLock写锁关系
 *
 * 4总结
 * ①Java并发库中ReentrantReadWriteLock实现了ReadWriteLock接口并添加了可重入的特性
 * ②ReentrantReadWriteLock读写锁的效率明显高于synchronized关键字
 * ③ReentrantReadWriteLock读写锁的实现中，读锁使用共享模式；写锁使用独占模式，换句话说，
 * 读锁可以在没有写锁的时候被多个线程同时持有，写锁是独占的
 * ④ReentrantReadWriteLock读写锁的实现中，需要注意的，当有读锁时，写锁就不能获得；而当
 * 有写锁时，除了获得写锁的这个线程可以获得读锁外，其他线程不能获得读锁
 */
public class Test4 {

    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        // 同时读、写
        //testReadWrite();

        // 同时写
        //testWrite();

        // 同时读
        testRead();
    }

    private static void testRead() {
        // 同时读
        ExecutorService service = Executors.newCachedThreadPool(); // 一池N线程
        service.execute(() -> {
            readFile(Thread.currentThread());
        });

        service.execute(() -> {
            readFile(Thread.currentThread());
        });

        /**
         * 运行结果：
         * 当前为读锁！
         * 当前为读锁！
         * pool-1-thread-1:正在进行读操作......
         * pool-1-thread-2:正在进行读操作......
         * pool-1-thread-2:正在进行读操作......
         * pool-1-thread-1:正在进行读操作......
         * pool-1-thread-2:正在进行读操作......
         * pool-1-thread-1:正在进行读操作......
         * pool-1-thread-2:正在进行读操作......
         * pool-1-thread-1:正在进行读操作......
         * pool-1-thread-1:正在进行读操作......
         * pool-1-thread-1:读操作完毕！
         * 释放读锁！
         * pool-1-thread-2:正在进行读操作......
         * pool-1-thread-2:读操作完毕！
         * 释放读锁！
         *
         */
    }

    private static void testWrite() {
        // 同时写
        ExecutorService service = Executors.newCachedThreadPool(); // 一池N线程
        service.execute(() -> {
            writeFile(Thread.currentThread());
        });

        service.execute(() -> {
            writeFile(Thread.currentThread());
        });

        /**
         * 运行结果：
         * 当前为写锁！
         * pool-1-thread-1:正在进行写操作......
         * pool-1-thread-1:正在进行写操作......
         * pool-1-thread-1:正在进行写操作......
         * pool-1-thread-1:正在进行写操作......
         * pool-1-thread-1:正在进行写操作......
         * pool-1-thread-1:写操作完毕！
         * 释放写锁！
         * 当前为写锁！
         * pool-1-thread-2:正在进行写操作......
         * pool-1-thread-2:正在进行写操作......
         * pool-1-thread-2:正在进行写操作......
         * pool-1-thread-2:正在进行写操作......
         * pool-1-thread-2:正在进行写操作......
         * pool-1-thread-2:写操作完毕！
         * 释放写锁！
         *
         */}

    private static void testReadWrite() {
        // 同时读、写
        ExecutorService service = Executors.newCachedThreadPool(); // 一池N线程
        service.execute(() -> {
            readFile(Thread.currentThread());
        });

        service.execute(() -> {
            writeFile(Thread.currentThread());
        });

        /**
         * 运行结果：
         * 当前为读锁！
         * pool-1-thread-1:正在进行读操作......
         * pool-1-thread-1:正在进行读操作......
         * pool-1-thread-1:正在进行读操作......
         * pool-1-thread-1:正在进行读操作......
         * pool-1-thread-1:正在进行读操作......
         * pool-1-thread-1:读操作完毕！
         * 释放读锁！
         * 当前为写锁！
         * pool-1-thread-2:正在进行写操作......
         * pool-1-thread-2:正在进行写操作......
         * pool-1-thread-2:正在进行写操作......
         * pool-1-thread-2:正在进行写操作......
         * pool-1-thread-2:正在进行写操作......
         * pool-1-thread-2:写操作完毕！
         * 释放写锁！
         *
         * 结论：读写锁的实现必须确保写操作对读操作的内存影响。换句话说，一个获得了读锁的线程
         * 必须能看到前一个释放的写锁所更新的内容，读写锁之间为互斥。
         */}

    private static void readFile(Thread thread) {
        lock.readLock().lock();
        boolean readLock = lock.isWriteLocked();
        if (!readLock) {
            System.out.println("当前为读锁！");
        }
        try {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(thread.getName() + ":正在进行读操作......");
            }
            System.out.println(thread.getName() + ":读操作完毕！");
        } finally {
            System.out.println("释放读锁！");
            lock.readLock().unlock();
        }
    }

    private static void writeFile(Thread thread) {
        lock.writeLock().lock();
        boolean writeLock = lock.isWriteLocked();
        if (writeLock) {
            System.out.println("当前为写锁！");
        }
        try {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(thread.getName() + ":正在进行写操作......");
            }
            System.out.println(thread.getName() + ":写操作完毕！");
        } finally {
            System.out.println("释放写锁！");
            lock.writeLock().unlock();
        }
    }
}
