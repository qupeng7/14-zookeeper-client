package com.qupeng.service;


import com.qupeng.model.Goods;

import java.io.FileNotFoundException;

public interface GoodsService {

	public Goods selectByPrimaryKey(Integer id);

	/**
	 * 减库存
	 *
	 * @param id
	 * @return
	 * @throws FileNotFoundException
	 * @throws InterruptedException
	 */
	public int updateByPrimaryKeyStore(Integer id);

}