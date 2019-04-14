package com.qetch.springmvc.test.zookeeper;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WatchMore extends BaseZKConfig {
    private static final Logger logger = LoggerFactory.getLogger(WatchMore.class);

    public static final String PATH = "/helloZK2";

    private String oldValue = "";
    private String newValue = "";

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public void startZK() throws IOException {
        ZooKeeper zk = new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

            }
        });
        setZk(zk);
    }

    public void createZnode(String path, String data) throws KeeperException, InterruptedException {
        getZk().create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }

    public String getZnode(String path) throws KeeperException, InterruptedException {
        String result = "";
        byte[] data = getZk().getData(path, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    triggerValue(path);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, new Stat());
        result = new String(data);
        oldValue = result;
        return result;
    }

    private boolean triggerValue(String path) throws KeeperException, InterruptedException {
        String result = "";
        byte[] data = getZk().getData(path, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    triggerValue(path);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, new Stat());
        result = new String(data);
        newValue = result;
        if (newValue.equals(oldValue)) {
            logger.info("**********The value no changes**********");
            return false;
        } else {
            oldValue = newValue;
            logger.info("**********oldValue:" + oldValue + "\t newValue:" + newValue + "**********");
            return true;
        }
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        WatchMore watchMore = new WatchMore();
        watchMore.startZK();
        if (watchMore.getZk().exists(PATH, false) == null) {
            watchMore.createZnode(PATH, "AAA");

            String result = watchMore.getZnode(PATH);

            logger.info("main init result:" + result);
        } else {
            logger.info("This node is exists");
        }

        Thread.sleep(Integer.MAX_VALUE);
    }
}