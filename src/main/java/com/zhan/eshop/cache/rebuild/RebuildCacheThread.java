package com.zhan.eshop.cache.rebuild;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhan.eshop.cache.model.ProductInfo;
import com.zhan.eshop.cache.service.CacheService;
import com.zhan.eshop.cache.spring.SpringContextHolder;
import com.zhan.eshop.cache.zk.ZooKeeperSession;

/**
 * 缓存重建线程
 *
 * @author Administrator
 *
 */
public class RebuildCacheThread implements Runnable {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void run() {
		RebuildCacheQueue rebuildCacheQueue = RebuildCacheQueue.getInstance();
		ZooKeeperSession zkSession = ZooKeeperSession.getInstance();
		CacheService cacheService = (CacheService) SpringContextHolder.getApplicationContext()
            .getBean("cacheService");
		
		while(true) {
			ProductInfo productInfo = rebuildCacheQueue.takeProductInfo();

			// 获取锁
			zkSession.acquireDistributedLock(productInfo.getId());  
			
			ProductInfo existedProductInfo = cacheService.getProductInfoFromReidsCache(productInfo.getId());
			
			if(existedProductInfo != null) {
				// 比较当前数据的时间版本比已有数据的时间版本是新还是旧
				try {
					Date date = sdf.parse(productInfo.getModifiedTime());
					Date existedDate = sdf.parse(existedProductInfo.getModifiedTime());
					
					if(date.before(existedDate)) {
						System.out.println("current date[" + productInfo.getModifiedTime() + "] is before existed date[" + existedProductInfo.getModifiedTime() + "]");
						// 当前数据的时间比redis中已有的数据时间旧，不处理，进行下一个循环
						continue;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("current date[" + productInfo.getModifiedTime() + "] is after existed date[" + existedProductInfo.getModifiedTime() + "]");
			} else {
				System.out.println("existed product info is null......");   
			}

			// 新，则覆盖原先的缓存
			cacheService.saveProductInfo2LocalCache(productInfo);
            try {
                cacheService.saveProductInfo2ReidsCache(productInfo);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            // 释放锁
            zkSession.releaseDistributedLock(productInfo.getId());
		}
	}

}
