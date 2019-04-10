package com.qetch.springmvc.test.juc.reentrantlock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 1.3锁降级
 * 要实现一个读写锁，需要考虑很多细节，其中之一就是锁升级和锁降级的问题。什么是升级和降级呢？
 * ReadWriteLock的javadoc有一段话：
 * 在不允许中间写入的情况下，写入锁可以降级为读锁吗？读锁是否可以升级为写锁，优先于其他等待的读取
 * 或写入操作？简言之就是说，锁降级：从写锁变成读锁；锁升级：从读锁变成写锁，ReadWriteLock是否
 * 支持呢？
 */
public class Test2 {

    public static void main(String[] args) {
        // 测试锁升级（读锁->写锁）
        //testUpgradeLock();

        // 测试锁降级（写锁->读锁）
        testDowngradeLock();
    }

    private static void testUpgradeLock() {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        lock.readLock().lock();
        System.out.println("get readLock");

        lock.writeLock().lock();
        System.out.println("blocking");

        /**
         * 运行结果：get readLock
         * 结论：上面的测试代码会产生死锁，因为同一个线程中，在没有释放读锁的情况下，就去
         * 申请写锁，这属于锁升级，ReentrantReadWriteLock是不支持的。
         */
    }

    private static void testDowngradeLock() {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        lock.writeLock().lock();
        System.out.println("writeLock");

        lock.readLock().lock();
        System.out.println("get read lock");

        /**
         * 运行结果：
         * writeLock
         * get read lock
         * 结论：ReentrantReadWriteLock支持锁降级，上面代码不会产生死锁。这段代码
         * 虽然不会导致死锁，但没有正确的释放锁。从写锁降级成读锁，并不会自动释放当前线程
         * 获取的写锁，仍然需要显示的释放，否则别的线程永远也获取不到写锁。
         */
    }
}
