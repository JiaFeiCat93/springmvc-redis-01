package com.qetch.springmvc.test.juc;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ShareResource {
    private int number = 0;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

}

public class ReadWriteLockDemo {
}
