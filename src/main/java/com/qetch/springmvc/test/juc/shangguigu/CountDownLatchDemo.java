package com.qetch.springmvc.test.juc.shangguigu;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch:
 * 让一些线程阻塞，直到另一些线程完成一系列操作后才被唤醒。
 *
 * CountDownLatch主要有两个方法：当一个或多个线程调用await()方法时，这些线程会阻塞；
 * 其他线程调用countDown()方法会将计数器减1（调用countDown()方法的线程不会阻塞）。
 * 当计数器的值变为0时，因await()方法阻塞的线程会被唤醒，继续执行。
 *
 * 解释：6个同学陆续离开教室后，值班同学才可以关门。
 * 也即 秦灭六国，一统华夏
 * main主线程必须要等前面6个线程完成全部工作后，自己才能继续往下执行。
 */
public class CountDownLatchDemo {

    public static void main(String[] args) {
        //testCloseDoor();
        CountDownLatch cdl = new CountDownLatch(6);
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t 国被灭");
                cdl.countDown();
            }, CountryEnum.forEachCountryEnum(i).getRetMsg()).start();
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "************秦灭六国，一统华夏");
    }

    private static void testCloseDoor() {
        CountDownLatch cdl = new CountDownLatch(6);
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t 下自习离开教室");
                cdl.countDown();
            }, String.valueOf(i)).start();
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "************班长锁门走人");
    }
}
