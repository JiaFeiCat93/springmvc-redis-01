package com.qetch.springmvc.test.juc.shangguigu;

class ShareData {
    private int number = 0;

    public synchronized void increment() throws InterruptedException {
        /*if (number != 0) {
            this.wait();
        }*/
        while (number != 0) {
            this.wait();
        }
        number++;
        System.out.println(Thread.currentThread().getName() + "\t" + number);
        this.notifyAll();
    }

    public synchronized void decrement() throws InterruptedException {
        /*if (number == 0) {
            this.wait();
        }*/
        while (number == 0) {
            this.wait();
        }
        number--;
        System.out.println(Thread.currentThread().getName() + "\t" + number);
        this.notifyAll();
    }
}

/**
 * 有两个线程：
 * 可以操作初始值为0的一个变量，
 * 实现一个线程对该变量加1，另一个线程对该变量减1
 * 交替，来10轮，变量初始值为0
 */
public class NotifyWaitDemo {

    public static void main(String[] args) {
        ShareData shareData = new ShareData();
        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    shareData.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "AA").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    shareData.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "BB").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    shareData.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "CC").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    shareData.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "DD").start();
    }
}
