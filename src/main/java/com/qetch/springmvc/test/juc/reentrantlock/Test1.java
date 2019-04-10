package com.qetch.springmvc.test.juc.reentrantlock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 1.1获取锁顺序
 * 非公平模式（默认）
 * 当以非公平初始化时，读锁和写锁的获取的顺序是不确定的。非公平锁主张竞争获取，可能会延缓一个或
 * 多个读或写线程，但是会比公平锁有更高的吞吐量。
 * 公平模式
 * 当以公平模式初始化时，线程将会以队列的顺序获取锁。当当前线程释放锁后，等待时间最长的写锁线程
 * 就会被分配写锁；或者有一组读线程组等待时间比写线程长，那么这组读线程组将会被分配读锁。
 *
 * 1.2可重入
 * 什么是可重入锁，不可重入锁呢？“重入”字面意思已经很明显了，就是可以重新进入。可重入锁，就是
 * 说一个线程在获取某个锁后，还可以继续获取该锁，即允许一个线程多次获取同一个锁。比如
 * synchronized内置锁就是可重入的，如果A类有2个synchronized方法method1和method2，那么
 * method1调用method2是允许的。显然重入锁给编程带来了极大的方便。假如内置锁不是可重入的，那么
 * 导致的问题是：1个类的synchronized方法不能调用本类其他synchronized方法，也不能调用父类中
 * 的synchronized方法。与内置锁对应，JDK提供的显示锁ReentrantLock也是可以重入的，这里通过
 * 一个例子着重说下可重入锁的释放需要注意的事。
 */
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
        lock.writeLock().unlock();

        /**
         * 运行结果：release one lock
         * 分析：从运行结果中，可以看出，程序并未执行线程的run()方法，由此我们可知，上面的代码
         * 会出现死锁，因为主线程2次获取了锁，但是却只释放了1次锁，导致线程thread永远也不能获取
         * 锁。
         * 一个线程获取多少次锁，就必须释放多少次锁。这对于内置锁也是适用的，每一次进入和离开
         * synchronized方法（代码块），就是一次完整的锁获取与释放。
         */

        /**
         * 如果把上面第26行代码注释打开并运行main()方法，则运行结果为：
         * release one lock
         * Thread real execute
         * 即线程thread的run()方法执行了。
         */
    }
}
