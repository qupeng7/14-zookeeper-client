/**
 * @项目名：zjsProject
 * @创建人： qupeng
 * @创建时间： 2019-08-23
 * @公司： www.qupeng.com
 * @描述：TODO
 */

package com.qupeng.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * <p>NAME: CuratorClient</p>
 * @author qupeng
 * @date 2019-08-23 15:17:14
 * @version 1.0
 */

public class CuratorClient {
    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    private static final String ROOT_NODE = "/root";

    public CuratorFramework client = null;

    //如何连接zookeeper
    public CuratorClient() {
        //重试策略
        RetryPolicy retry = new RetryNTimes(3, 2000);

        //创建zookeeper连接，新版本
        client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, retry);

        //老版本创建连接客户端
//        client = CuratorFrameworkFactory.builder().
//                connectString(ZK_ADDRESS).
//                sessionTimeoutMs(5000).
//                connectionTimeoutMs(10000).
//                retryPolicy(retry).
//                build();

        //启动客户端
        client.start();
    }

    public static void main(String[] args) throws Exception {
        CuratorClient curatorClient=new CuratorClient();
        System.out.println("zookeeper连接成功：" + curatorClient.client);

        //连接状态
        //CuratorFrameworkState state = curatorClient.client.getState();
        //System.out.println(state.name());


//        String node = curatorClient.client.create().forPath(ROOT_NODE, "curator zookeeper client".getBytes());
//        System.out.println(node);

        //创建节点, curator客户端开发是一种流式编码方式，不断地点.点.  构建器模式
//        String node = curatorClient.client.create()
//                .creatingParentsIfNeeded()
//                .withMode(CreateMode.PERSISTENT)
//                .forPath(ROOT_NODE + "/home", "curator zookeeper client".getBytes());
//        System.out.println(node);

        //更新节点，更新的时候如果指定数据版本的话，那么需要和zookeeper中当前数据的版本要一致，-1表示匹配任何版本
//        Stat stat = curatorClient.client.setData().forPath(ROOT_NODE + "/home", "update curator zookeeper client".getBytes());
//        System.out.println(stat.toString());

        //读取zookeeper的数据
//        byte[] bytes =curatorClient.client.getData().forPath(ROOT_NODE);
//        System.out.println(new String(bytes));
//        Stat stat1 = new Stat();
//        byte[] bytes = curatorClient.client.getData().storingStatIn(stat1).forPath(ROOT_NODE);
//        System.out.println(new String(bytes));

        //查询子节点
//        List<String> strings = curatorClient.client.getChildren().forPath(ROOT_NODE);
//        System.out.println(strings.size());
//        strings.forEach((String s)->{
//            System.out.println(s.toString());
//        });

        //方法引用
        /**
         * 1、方法 forEach
         * void forEach(Consumer<? super T> action)
         *
         * 2、Consumer对象，是函数式接口
         * -->函数式接口如何去表达（怎么把这个接口创建出一个对象实例出来）
         *    1、匿名内部类表达
         *    2、lambda表达式去表达
         *    3、用方法引用去表达 ： 如果你有一个方法已经可以实现Consumer接口里面的抽象方法：void accept(T t);
         *
         *    函数式接口的抽象方法：
         *    void accept(T t);
         *
         *    //打印的方法
         *    public void println(String x) {
         *         synchronized (this) {
         *             print(x);
         *             newLine();
         *         }
         *     }
         *
         *    方法引用是对lambda表达式的再一次简化
         */
        //PrintStream ps = System.out;
        //strings.forEach(ps::println);

        //方法引用，引用自己定义的方法
        //strings.forEach(curatorClient::showInfo);

        //节点是否存在判断
//        Stat stat2 = curatorClient.client.checkExists().forPath(ROOT_NODE + "/home");
//        if (stat2==null) {
//            System.out.println("节点不存在");
//        } else {
//            System.out.println("节点存在");
//        }

        //删除节点
//        curatorClient.client.delete().forPath(ROOT_NODE);//这样不能删除子节点
        //子节点也可以一同删除
//        curatorClient.client.delete().deletingChildrenIfNeeded().forPath(ROOT_NODE);
        //guaranteed保证这个删除命令一定会执行,确保节点一定会删除
//        curatorClient.client.delete().guaranteed().deletingChildrenIfNeeded().forPath(ROOT_NODE);


        //zookeeper节点的监听 (watcher)，此监听是一次性，只能监听一次
//        curatorClient.client.getData().usingWatcher((Watcher) (WatchedEvent event)->{
//            System.out.println("对节点 [" +event.getPath()+ "] 进行了操作，操作事件：" + event.getType().name());
//            System.out.println("catcher: " + Thread.currentThread().getId());
//        }).forPath(ROOT_NODE);

        //后台执行，拿不到数据
//        byte[] bytes1 = curatorClient.client.getData().inBackground().withUnhandledErrorListener(
//                (String message, Throwable e) -> {
//                    //监听线程
//                    System.out.println("catcher: " + Thread.currentThread().toString());
//                    System.out.println(message);
//                    System.out.println(e.getMessage());
//                }
//        ).forPath(ROOT_NODE);
//        System.out.println(new String(bytes1));

        //监听节点，对具体节点(当前节点)进行监听，可以一直监听
//        NodeCache nodeCache = new NodeCache(curatorClient.client, ROOT_NODE + "/home");
//        nodeCache.start(true);//true 初始化的时候,就拿到节点的数据
//        if (nodeCache.getCurrentData() != null) {
//            ChildData childData = nodeCache.getCurrentData();
//            byte[] bytes11 = childData.getData();
//            System.out.println("缓存节点数据：" + new String(bytes11));
//        } else {
//            System.out.println("缓存节点数据是空的.....");
//        }
        //添加 NodeCacheListener
//        nodeCache.getListenable().addListener( () -> {
//            System.out.println("监听到事件了........");
//            ChildData childData = nodeCache.getCurrentData();
//            byte[] bytes= childData.getData();
//            System.out.println("缓存节点数据1：" + new String(bytes));
//            System.out.println("缓存节点1：" + nodeCache.getPath());
//        });




        //PathChildrenCacheListener
        //当前节点的子节点监听,不能递归监听，子节点下的子节点不能递归监控
        /**
         * 启动模式：
         * 1）NORMAL——异步初始化cache
         *
         * （2）BUILD_INITIAL_CACHE——同步初始化cache
         *
         * （3）POST_INITIALIZED_EVENT——异步初始化cache，并触发完成事件
         */
//        PathChildrenCache childrenCache = new PathChildrenCache(curatorClient.client, ROOT_NODE, true);
//        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
//        childrenCache.getListenable().addListener(
//                //PathChildrenCacheListener
//                (CuratorFramework client, PathChildrenCacheEvent event) -> {
//                    System.out.println("子节点监听执行了99999999..........");
//
//                    byte[] bytes = event.getData().getData();
//                    System.out.println("111: "+ new String(bytes));
//
//                    PathChildrenCacheEvent.Type type = event.getType();
//                    System.out.println("111: "+ type.name());
//                }
//        );

        //对指定节点下的所有节点进行监听
        //添加 TreeCacheListener
        /**
         * TreeCache 的事件类型为：
         *
         * （1）NODE_ADDED
         *
         * （2）NODE_UPDATED
         *
         * （3）NODE_REMOVED
         *
         * 这一点，与Path Cache 的事件类型不同，与Path Cache 的事件类型为：
         *
         * （1）CHILD_ADDED
         *
         * （2）CHILD_UPDATED
         *
         * （3）CHILD_REMOVED
         *  ————————————————
         */
        TreeCache treeCache = new TreeCache(curatorClient.client, ROOT_NODE);
        treeCache.start();
        treeCache.getListenable().addListener((CuratorFramework client, TreeCacheEvent event) -> {
            System.out.println("所有子节点监听执行了99999999..........");
            byte[] bytes1 = event.getData().getData();
            System.out.println("222: "+ new String(bytes1));

            TreeCacheEvent.Type type = event.getType();
            System.out.println("222: "+ type.name());
        });
        //休眠时间长一点，以便于测试事件的监听
        Thread.sleep(1000000000);
    }

    // void accept(T t);
    public void showInfo (String str) {
        System.out.println("自己方法引用：" + str);
    }

}
