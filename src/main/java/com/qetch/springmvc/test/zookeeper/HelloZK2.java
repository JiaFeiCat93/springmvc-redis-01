package com.qetch.springmvc.test.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HelloZK2 extends BaseZKConfig {
    private static final Logger logger = LoggerFactory.getLogger(HelloZK2.class);

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
        byte[] data = getZk().getData(path, false, new Stat());
        result = new String(data);
        return result;
    }

    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        HelloZK2 helloZK2 = new HelloZK2();
        helloZK2.startZK();
        ZooKeeper zk = helloZK2.getZk();
        if (zk.exists(PATH, false) == null) {
            helloZK2.createZnode(PATH, "hello zk2!");
            String result = helloZK2.getZnode(PATH);
            logger.info("result:" + result);
        } else {
            logger.info("This node is exists");
        }
        //helloZK2.stopZK();
    }
}
