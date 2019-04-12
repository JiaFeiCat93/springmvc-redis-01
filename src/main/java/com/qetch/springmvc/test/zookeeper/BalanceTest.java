package com.qetch.springmvc.test.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BalanceTest {
    private static final Logger logger = LoggerFactory.getLogger(BalanceTest.class);

    private static final String CONNECT_STRING = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 50 * 1000;
    private static final String PATH = "/bank";
    private static final String SUB_PREFIX = "sub";

    List<String> list = new ArrayList<>();
    private int index = 0;
    private int totalWindows = 5;

    private ZooKeeper zk = null;

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public ZooKeeper startZK() throws IOException {
        return new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    list = zk.getChildren(PATH, true, new Stat());
                    System.out.println("******" + list);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String doRequest() throws KeeperException, InterruptedException {
        index = index + 1;
        for (int i = index; i <= totalWindows; i++) {
            if (list.contains(SUB_PREFIX + index)) {
                byte[] data = zk.getData(PATH + "/" + SUB_PREFIX + index, true, new Stat());
                return new String(data);
            } else {
                index = index + 1;
            }
        }
        for (int i = 1; i <= totalWindows; i++) {
            if (list.contains(SUB_PREFIX + i)) {
                index = i;
                byte[] data = zk.getData(PATH + "/" + SUB_PREFIX + index, true, new Stat());
                return new String(data);
            }
        }
        return "**********No this window**********";
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        BalanceTest test = new BalanceTest();
        test.setZk(test.startZK());
        Thread.sleep(3000);
        for (int i = 1; i <= 15; i++) {
            Thread.sleep(1500);
            String result = test.doRequest();
            System.out.println("CustomerID:" + i + "\t currentWindow:" + test.index + "\t result:" + result);
        }
    }
}
