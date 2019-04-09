package com.qetch.springmvc.test.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * 创建线程方式三：实现Callable
 */
class MyThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        TimeUnit.SECONDS.sleep(4);
        System.out.println(Thread.currentThread().getName() + "\t **************call()");
        return 110;
    }
}

/**
 * 在主线程中需要执行比较耗时的操作时，但又不想阻塞主线程时，可以把这些作业交给Future对象在后台完成，
 * 当主线程将来需要时，就可以通过Future对象获得后台作业的计算结果或执行状态。
 *
 * 一般FutureTask多用于耗时的计算，主线程可以在完成自己的任务后，再去获取结果。
 *
 * 仅在计算完成时才能检索结果，如果计算尚未完成，则阻塞get()方法。
 * 一旦计算完成，就不能再重新开始或取消计算。get()方法获取结果只有在计算完成时获取，否则会一直阻塞直到任务转入完成状态，
 * 然后会返回结果或者抛出异常。
 *
 * 只计算一次，get()方法放到最后。
 */
public class CallableDemo {

    public static void main(String[] args) {
        //testCallable();
        testLambdaCallable();
    }

    private static void testLambdaCallable() {
        try {
            FutureTask<Integer> futureTask = new FutureTask<Integer>(() -> {
                TimeUnit.SECONDS.sleep(4);
                System.out.println(Thread.currentThread().getName() + "\t **************call()");
                return 110;
            });
            new Thread(futureTask, "AA").start();
            new Thread(futureTask, "BB").start();

            System.out.println(Thread.currentThread().getName() + "\t *************主线程");


            Integer result1 = futureTask.get(); // 尽量把get()方法放到最后，放在前面会阻塞
            Integer result2 = futureTask.get();
            System.out.println("************result1:" + result1);
            System.out.println("************result2:" + result2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void testCallable() {
        try {
            MyThread myThread = new MyThread();
            FutureTask<Integer> futureTask = new FutureTask<Integer>(myThread);
            new Thread(futureTask, "AA").start(); // 一个FutureTask只能执行一个线程
            new Thread(futureTask, "BB").start();

            System.out.println(Thread.currentThread().getName() + "\t *************主线程");


            Integer result1 = futureTask.get(); // 尽量把get()方法放到最后，放在前面会阻塞主线程
            Integer result2 = futureTask.get();
            System.out.println("************result1:" + result1);
            System.out.println("************result2:" + result2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
