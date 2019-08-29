/**
 * @项目名：zjsProject
 * @创建人： qupeng
 * @创建时间： 2019-08-29
 * @公司： www.bjpowernode.com
 * @描述：TODO
 */

package com.qupeng.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>NAME: CuratorAcl</p>
 * @author qupeng
 * @date 2019-08-29 21:42:24
 * @version 1.0
 */

public class CuratorAcl {

    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    private static final String ROOT_NODE = "/root2";

    CuratorFramework client = null;
    //如何连接zookeeper
    public CuratorAcl() {
        //重试策略
        RetryPolicy retry = new RetryNTimes(3, 2000);
        //创建zookeeper连接，新版本
        //client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, retry);
        //老版本创建连接客户端
        //authorization("digest", "seven:123456".getBytes())  用什么账号去连接zookeeper,如果权限不够,就不能修改节点,删除节点
        client = CuratorFrameworkFactory.builder().
                //用户认证
                authorization("digest", "seven:123456".getBytes()).
                connectString(ZK_ADDRESS).
                sessionTimeoutMs(5000).
                connectionTimeoutMs(10000).
                retryPolicy(retry).
                build();
        //启动客户端
        client.start();
    }
    public static void main(String[] args) throws Exception {
        CuratorAcl curatorAcl = new CuratorAcl();
        System.out.println("zookeeper连接成功：" + curatorAcl.client);
        CuratorFrameworkState state = curatorAcl.client.getState();
        System.out.println(state.name());


        Id seven = new Id("digest", DigestAuthenticationProvider.generateDigest("seven:123456"));
        Id zhangsan = new Id("digest", DigestAuthenticationProvider.generateDigest("zhangsan:123456"));

        List<ACL> aclList = new ArrayList<ACL>();
        aclList.add(new ACL(ZooDefs.Perms.ALL, seven));//所有权限
        aclList.add(new ACL(ZooDefs.Perms.READ, zhangsan));//读取权限

        //完全开放的节点权限
//        String node = curatorAcl.client.create()
//                .creatingParentsIfNeeded()
//                .withMode(CreateMode.PERSISTENT)
//                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)//指定Acl
//                .forPath(ROOT_NODE + "/home", "curator zookeeper client".getBytes());

        //创建节点, curator客户端开发是一种流式编码方式，不断地点.点.
//        String node = curatorAcl.client.create()
//                .creatingParentsIfNeeded()
//                .withMode(CreateMode.PERSISTENT)
//                .withACL(aclList)//指定Acl
//                .forPath(ROOT_NODE + "/home", "curator zookeeper client".getBytes());
//
//        System.out.println(node);

        //对节点的修改权限
        Stat stat = curatorAcl.client.setData().withVersion(-1).forPath(ROOT_NODE + "/home", "update curator zookeeper client".getBytes());
        System.out.println(stat.toString());

        //读取zookeeper的数据
        Stat stat1 = new Stat();
        byte[] bytes = curatorAcl.client.getData().storingStatIn(stat1).forPath(ROOT_NODE + "/home");
        System.out.println(new String(bytes));
        System.out.println(stat1);

        //把ROOT_NODE节点设置三个用户的权限，修改一下该节点所属用户   给某个几点设置权限
        curatorAcl.client.setACL().withACL(aclList).forPath(ROOT_NODE);

    }
}
