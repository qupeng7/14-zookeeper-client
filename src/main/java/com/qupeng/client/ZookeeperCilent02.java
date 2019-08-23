/**
 * @项目名：zjsProject
 * @创建人： qupeng
 * @创建时间： 2019-08-23
 * @公司： www.bjpowernode.com
 * @描述：TODO
 */

package com.qupeng.client;

import com.qupeng.serializer.MyZkSerializer;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

/**
 * <p>NAME: ZookeeperCilent01</p>
 * @author qupeng
 * @date 2019-08-23 14:14:03
 * @version 1.0
 */

public class ZookeeperCilent02 {
    private static final String zk_address="127.0.0.1:2181";

    private static final String ROOT_NODE="/root";


    public static void main(String[] args) throws Exception {
        //建立zookeeper连接
        ZkClient client = new ZkClient(zk_address, 5000, 15000, new MyZkSerializer());

        //创建一个持久化节点
        //client.createPersistent(ROOT_NODE);
        //创建节点
        //String node = client.create(ROOT_NODE + "/home", "zkclient data", CreateMode.PERSISTENT);

        //System.out.println(node);

        //删除节点
//        client.delete(ROOT_NODE + "/home");

        //节点是否存在
//        boolean boo=client.exists(ROOT_NODE);
//        System.out.println(boo);

        //修改节点数据
        client.writeData(ROOT_NODE ,"zookeeper data");
        //读取节点数据
       String data= client.readData(ROOT_NODE);
        System.out.println(data);


    }
}
