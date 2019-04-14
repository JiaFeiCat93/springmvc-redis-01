package com.qetch.springmvc.test.zookeeper;

import org.apache.zookeeper.ZooKeeper;

public class BaseZKConfig {
    public static final String CONNECT_STRING = "127.0.0.1:2181";
    public static final int SESSION_TIMEOUT = 20 * 1000;

    private ZooKeeper zk;

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public void stopZK() throws InterruptedException {
        if (zk != null) {
            zk.close();
        }
    }
}
