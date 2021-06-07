package com.zhan.eshop.cache.prewarm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhan.eshop.cache.model.ProductInfo;
import com.zhan.eshop.cache.service.CacheService;
import com.zhan.eshop.cache.spring.SpringContextHolder;
import com.zhan.eshop.cache.zk.ZooKeeperSession;

/**
 * 缓存预热线程
 *
 * @author zhanzhan
 *
 */
public class CachePrewarmThread extends Thread {
	
	@Override
	public void run() {
		CacheService cacheService = (CacheService) SpringContextHolder.
				getApplicationContext().getBean("cacheService"); 

		ZooKeeperSession zkSession = ZooKeeperSession.getInstance();

		// 获取storm taskid列表
		String taskidList = zkSession.getNodeData("/taskid-list"); 
		
		System.out.println("【CachePrwarmThread获取到taskid列表】taskidList=" + taskidList);  
		
		if(taskidList != null && !"".equals(taskidList)) {
			String[] taskidListSplited = taskidList.split(",");  
			for(String taskid : taskidListSplited) {
				String taskidLockPath = "/taskid-lock-" + taskid;

				// 尝试获取taskid分布式锁
				boolean result = zkSession.acquireFastFailedDistributedLock(taskidLockPath);
				if(!result) {
					continue;
				}

				// 获取task预热状态锁
				String taskidStatusLockPath = "/taskid-status-lock-" + taskid;
				zkSession.acquireDistributedLock(taskidStatusLockPath);  

				// 获取预热状态
				String taskidStatus = zkSession.getNodeData("/taskid-status-" + taskid);
				System.out.println("【CachePrewarmThread获取task的预热状态】taskid=" + taskid + ", status=" + taskidStatus);

                // 状态为空说明没有被预热
				if("".equals(taskidStatus)) {
					String productidList = zkSession.getNodeData("/task-hot-product-list-" + taskid);
					System.out.println("【CachePrewarmThread获取到task的热门商品列表】productidList=" + productidList); 
					JSONArray productidJSONArray = JSONArray.parseArray(productidList);

					// 开始预热
					for(int i = 0; i < productidJSONArray.size(); i++) {
						Long productId = productidJSONArray.getLong(i);
						// 模拟拿到了一条数据
						String productInfoJSON = "{\"id\": " + productId + ", \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1, \"modifiedTime\": \"2021-06-07 21:57:00\"}";
						ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
						cacheService.saveProductInfo2LocalCache(productInfo);
						System.out.println("【CachePrwarmThread将商品数据设置到本地缓存中】productInfo=" + productInfo);
                        try {
                            cacheService.saveProductInfo2ReidsCache(productInfo);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        System.out.println("【CachePrwarmThread将商品数据设置到redis缓存中】productInfo=" + productInfo);
					}
					
					zkSession.createNode("/taskid-status-" + taskid);
					// 设置预热状态，预热成功了
					zkSession.setNodeData("/taskid-status-" + taskid, "success");   
				}
				
				zkSession.releaseDistributedLock(taskidStatusLockPath);
				
				zkSession.releaseDistributedLock(taskidLockPath);
			}
		}
	}
	
}
