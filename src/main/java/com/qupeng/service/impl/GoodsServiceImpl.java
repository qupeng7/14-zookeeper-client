package com.qupeng.service.impl;

import com.qupeng.curator.CuratorClient;
import com.qupeng.mapper.GoodsMapper;
import com.qupeng.model.Goods;
import com.qupeng.service.GoodsService;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

/**
 * 当类上和方法上都有@Transactional注解，以方法上的注解为准
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private CuratorClient curatorClient;

	@Transactional(readOnly = true)
	public Goods selectByPrimaryKey(Integer id)
	{
		return goodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 采用Zookeeper分布式锁解决库存超卖问题
	 *
	 * @param id
	 * @return
	 */
	@Transactional(transactionManager="transactionManager", readOnly = false,
			isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED,
			noRollbackFor = FileNotFoundException.class, rollbackFor = Exception.class, timeout = -1)
	public int updateByPrimaryKeyStore(Integer id) {
		int update = 0;

		//创建一个Curator客户端提供的分布式互斥锁，是基于一个node节点创建分布式互斥锁
		InterProcessMutex lock = new InterProcessMutex(curatorClient.client, "/storeLock");

		try {
			//尝试获取zookeeper分布式锁
			if (lock.acquire(10000, TimeUnit.MILLISECONDS)) {

				//======以下是业务代码 =====

				Goods goods = goodsMapper.selectByPrimaryKey(id);

				//判断库存是否大于0
				if (goods.getStore() > 0) {

					//库存大于0，可以减库存，排它锁
					update = goodsMapper.updateByPrimaryKeyStore(id);

					if (update > 0) {
						System.out.println("减库存成功，可以下订单");

					} else {
						System.out.println("减库存失败，不能下订单");
						throw new RuntimeException();
					}

				} else {
					System.out.println("没有库存");
				}

				//======以上是业务代码 =====
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				//释放zookeeper分布式锁
				lock.release();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		//返回结果
		return update;
	}
}