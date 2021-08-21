package com.zhan.eshop.cache.hystrix.command;

import redis.clients.jedis.JedisCluster;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.zhan.eshop.cache.model.ProductInfo;
import com.zhan.eshop.cache.spring.SpringContextHolder;

public class GetProductInfoFromReidsCacheCommand extends HystrixCommand<ProductInfo> {

	private Long productId;
	
	public GetProductInfoFromReidsCacheCommand(Long productId) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RedisGroup"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionTimeoutInMilliseconds(100)
						.withCircuitBreakerRequestVolumeThreshold(1000)
						.withCircuitBreakerErrorThresholdPercentage(70)
						.withCircuitBreakerSleepWindowInMilliseconds(60 * 1000))
				);  
		this.productId = productId;
	}
	
	@Override
	protected ProductInfo run() throws Exception {
		JedisCluster jedisCluster = (JedisCluster) SpringContextHolder.getApplicationContext()
				.getBean("JedisClusterFactory"); 
		String key = "product_info_" + productId;
		String json = jedisCluster.get(key);
		if(json != null) {
			return JSONObject.parseObject(json, ProductInfo.class);
		}
		return null;
	}

    /**
     * 降级机制
     * @return
     */
	@Override
	protected ProductInfo getFallback() {
		return null;
	}

}
