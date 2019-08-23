/**
 * @项目名：zjsProject
 * @创建人： qupeng
 * @创建时间： 2019-08-23
 * @公司： www.bjpowernode.com
 * @描述：TODO
 */

package com.qupeng.client;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * <p>NAME: ZookeeperCilent01</p>
 * @author qupeng
 * @date 2019-08-23 14:14:03
 * @version 1.0
 */

public class ZookeeperCilent01 {
    private static final String zk_address="127.0.0.1:2181";

    private static final String ROOT_NODE="/root";

    //倒计数器，倒数1个
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {


        //创建zookeeper客户端对象
        ZooKeeper zooKeeper = new ZooKeeper(zk_address, 5000, new Watcher() {
            /**
             * 观察与zookeeper有没有连接上
             *
             * @param event
             */
            @Override
            public void process(WatchedEvent event) {
                //获取事件的状态
                Event.KeeperState state = event.getState();

                //获取事件类型
                Event.EventType type = event.getType();

                //如果已经建立上了连接
                if (Event.KeeperState.SyncConnected == state) {


                    if (Event.EventType.None == type) {
                        System.out.println("zookeeper连接成功......");
                        countDownLatch.countDown();
                    }
                }
            }
        });

        //倒计数器没有倒数完成，不能执行下面的代码，因为你要等它连上了，再操作
        countDownLatch.await();

        //下面可以开始操作zookeeper了
        //创建节点
       // String nodePath = zooKeeper.create(ROOT_NODE, "zookeeper".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //System.out.println("节点创建结果：" + nodePath);

       //String nodeChild = zooKeeper.create(ROOT_NODE + "/home", "zookeeper-home".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
       //System.out.println("子节点创建结果：" + nodeChild);

        //获取数据
        //byte[] bytes = zooKeeper.getData(ROOT_NODE + "/home", false, null);
        //System.out.println(new String(bytes));

        //修改数据
        //Stat stat = zooKeeper.setData(ROOT_NODE, "new zookeeper data".getBytes(), 0);
        //System.out.println(stat.getMtime());

        //zooKeeper.delete(ROOT_NODE+"/home", -1);

        Stat stat = zooKeeper.exists(ROOT_NODE, false);
        System.out.println(stat);

        //获取子节点
        List<String> list = zooKeeper.getChildren(ROOT_NODE, false);

        // default void forEach(Consumer<? super T> action)
        //用匿名内部类实现函数式接口
        Consumer consumer = new Consumer() {
            @Override
            public void accept(Object o) {
                System.out.println("遍历1：" + o.toString());
            }
        };
        list.forEach(consumer);

        list.forEach((Object o) -> System.out.println("遍历2：" + o.toString()));

        list.forEach((Object o) -> {
            System.out.println("遍历3：" + o.toString());
        });




    }
}
