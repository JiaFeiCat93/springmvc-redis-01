package com.qetch.springmvc.test.juc;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3); // 模拟3个停车位
        for (int i = 1; i <= 6; i++) { // 模拟6辆汽车
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "\t ***抢占了车位");
                    //TimeUnit.SECONDS.sleep(3); // 固定占用车位时间间隔
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5)); // 随机占用车位时间间隔
                    System.out.println(Thread.currentThread().getName() + "\t 离开了车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }, String.valueOf(i)).start();
        }
    }
}
