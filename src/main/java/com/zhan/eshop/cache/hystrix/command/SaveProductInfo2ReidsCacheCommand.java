package com.zhan.eshop.cache.hystrix.command;

import redis.clients.jedis.JedisCluster;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.zhan.eshop.cache.model.ProductInfo;
import com.zhan.eshop.cache.spring.SpringContextHolder;

public class SaveProductInfo2ReidsCacheCommand extends HystrixCommand<Boolean> {
	
	private ProductInfo productInfo;
	
	public SaveProductInfo2ReidsCacheCommand(ProductInfo productInfo) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RedisGroup"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        // 配置熔断策略
						.withExecutionTimeoutInMilliseconds(100)
						.withCircuitBreakerRequestVolumeThreshold(1000)
						.withCircuitBreakerErrorThresholdPercentage(70)
						.withCircuitBreakerSleepWindowInMilliseconds(60 * 1000))
				);  
		this.productInfo = productInfo;
	}
	
	@Override
	protected Boolean run() throws Exception {
		JedisCluster jedisCluster = (JedisCluster) SpringContextHolder.getApplicationContext()
				.getBean("JedisClusterFactory"); 
		String key = "product_info_" + productInfo.getId();
		jedisCluster.set(key, JSONObject.toJSONString(productInfo));  
		return true;
	}

    /**
     * 降级机制
     * @return
     */
	@Override
	protected Boolean getFallback() {
		return false;
	}
	
}
