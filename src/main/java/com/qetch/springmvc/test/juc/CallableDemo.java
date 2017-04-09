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
        System.out.println("**************call()");
        return 110;
    }
}

public class CallableDemo {

    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        FutureTask<Integer> futureTask = new FutureTask<Integer>(myThread);
        new Thread(futureTask, "AA").start();
        new Thread(futureTask, "BB").start();

        System.out.println("*************主线程");

        try {
            Integer result = futureTask.get();
            System.out.println("************result:" + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
