package com.qetch.springmvc.test.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HelloZK {
    private static final Logger logger = LoggerFactory.getLogger(HelloZK.class);

    private static final String CONNECT_STRING = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 20 * 1000;
    private static final String PATH = "/helloZK";

    /**
     * 获取ZK的连接实例
     * @return
     * @throws IOException
     */
    public ZooKeeper startZK() throws IOException {
        return new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

            }
        });
    }

    /**
     * 创建结点并赋值
     * @param zk
     * @param path
     * @param data
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void createZnode(ZooKeeper zk, String path, String data) throws KeeperException, InterruptedException {
        zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }

    /**
     * 获取结点值
     * @param zk
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String getZnode(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
        String result = "";

        byte[] data = zk.getData(path, false, new Stat());
        result = new String(data);

        return result;
    }

    /**
     * 关闭连接
     * @param zk
     * @throws InterruptedException
     */
    public void stopZK(ZooKeeper zk) throws InterruptedException {
        if (zk != null) {
            zk.close();
        }
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        HelloZK helloZK = new HelloZK();
        ZooKeeper zk = helloZK.startZK();
        String path = "/helloZK1";
        if (zk.exists(path, false) == null) {
            helloZK.createZnode(zk, path, "hello zk!");

            String result = helloZK.getZnode(zk, path);

            if (logger.isInfoEnabled()) {
                logger.info("main(String[])********** String result=" + result);
            }
        } else {
            logger.info("This node is exists");
        }
        //helloZK.stopZK(zk); // 关闭ZK会把上面新创建的结点删除掉，原因？？？
    }
}
