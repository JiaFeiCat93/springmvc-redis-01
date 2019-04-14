package com.qetch.springmvc.test.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WatchOne extends BaseZKConfig {
    private static final Logger logger = LoggerFactory.getLogger(WatchOne.class);

    public static final String PATH = "/helloZK2";

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
        return result;
    }

    private void triggerValue(String path) throws KeeperException, InterruptedException {
        String result = "";
        byte[] data = getZk().getData(path, null, new Stat());
        result = new String(data);
        logger.info("**********watcher after triggerValue result:" + result);
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        WatchOne watchOne = new WatchOne();
        watchOne.startZK();
        if (watchOne.getZk().exists(PATH, false) == null) {
            watchOne.createZnode(PATH, "AAA");

            String result = watchOne.getZnode(PATH);

            logger.info("main init result:" + result);
        } else {
            logger.info("This node is exists");
        }

        Thread.sleep(Integer.MAX_VALUE);
    }
}
