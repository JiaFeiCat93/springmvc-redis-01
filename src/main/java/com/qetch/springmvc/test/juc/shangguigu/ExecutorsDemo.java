package com.qetch.springmvc.test.juc.shangguigu;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 获取多线程的方式：线程池
 */
public class ExecutorsDemo {

    public static void main(String[] args) {
        //testThreadPool();
        testScheduledThreadPool();
    }

    private static void testScheduledThreadPool() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
        Future<Integer> result = null;
        try {
            for (int i = 0; i < 15; i++) {
                result = service.schedule(() -> {
                    System.out.print(Thread.currentThread().getName());
                    return new Random().nextInt(10);
                }, 2, TimeUnit.SECONDS); // 每隔2秒执行一次
                System.out.println("  ************" + result.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            service.shutdown();
        }
    }

    private static void testThreadPool() {
        //ExecutorService service = Executors.newFixedThreadPool(5); // 一池5线程
        //ExecutorService service = Executors.newSingleThreadExecutor(); // 一池1线程
        ExecutorService service = Executors.newCachedThreadPool(); // 一池N线程
        Future<Integer> result = null;
        try {
            for (int i = 0; i < 15; i++) {
                result = service.submit(() -> {
                    Thread.sleep(400);
                    System.out.print(Thread.currentThread().getName());
                    return new Random().nextInt(10);
                });
                System.out.println("  ************" + result.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            service.shutdown();
        }
    }
}
