/**
 * @项目名：zjsProject
 * @创建人： qupeng
 * @创建时间： 2019-09-02
 * @公司： www.qupeng.com
 * @描述：TODO
 */

package com.qupeng.id;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;

import javax.sound.midi.SoundbankResource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>NAME: IdGenerotor01</p>
 * @TODO: 2019/9/2  根据永久性顺序节点生成唯一id
 * @author qupeng
 * @date 2019-09-02 17:47:25
 * @version 1.0
 */

public class IdGenerotor01 {
    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    private static final String ID_NODE = "/id";

    CuratorFramework client = null;

    //倒计算器
    CountDownLatch countDownLatch = new CountDownLatch(1);

    //如何连接zookeeper
    public IdGenerotor01() {
        //重试策略
        RetryPolicy retry = new RetryNTimes(3, 2000);
        //创建zookeeper连接，新版本
        client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, retry);

        //启动客户端
        client.start();
    }

    /**
     * ID生成
     *
     * @return
     * @throws Exception
     */
    public String idGen() throws Exception {
        if (null == client.checkExists().forPath(ID_NODE)) {
            String node = client.create()
                    .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                    .forPath(ID_NODE);
            //避免占用zookeeper空间，返回有序节点后把节点删除一下
            client.delete().forPath(node);
            return node.replace(ID_NODE,"");
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        IdGenerotor01 idGenerotor = new IdGenerotor01();
//        idGenerotor.runThread();
        System.out.println(idGenerotor.idGen());
    }

    /**
     * 多线程并发执行
     */
    private void runThread() {
        //等待时间
        final long awaitTime = 5 * 1000;

        //创建一个确定的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(16);
        for (int i=0; i<16; i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //提交线程到线程池去执行
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        //等待，线程就位，但是不运行
                        countDownLatch.await();
                        System.out.println("Thread:"+Thread.currentThread().getName() + ", time: "+System.currentTimeMillis());

                        //执行业务代码
                        try {
                            String id = idGen();
                            System.out.println(id);
                        } catch (Throwable e) {
                            System.out.println("Thread:"+Thread.currentThread().getName() + e.getMessage());
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        //倒计算器 -1，那么16个线程就同时开始执行，那么就达到并发效果
        countDownLatch.countDown();

        //关闭线程池的代码
        try {
            // 传达完毕信号
            executorService.shutdown();
            // (所有的任务都结束的时候，返回TRUE)
            if(!executorService.awaitTermination(awaitTime, TimeUnit.MILLISECONDS)){
                // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
            System.out.println("awaitTermination interrupted: " + e);
            executorService.shutdownNow();
        }
    }
}
