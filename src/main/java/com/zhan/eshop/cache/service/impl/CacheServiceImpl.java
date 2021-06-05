package com.zhan.eshop.cache.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhan.eshop.cache.model.ProductInfo;
import com.zhan.eshop.cache.model.ShopInfo;
import com.zhan.eshop.cache.service.CacheService;

import redis.clients.jedis.JedisCluster;

/**
 * 缓存service实现类
 *
 * @author zhanzhan
 * @date 2021/5/23 20:26
 */
@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    public static final String CACHE_NAME = "local";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 将商品信息保存到本地缓存中
     *
     * value:指定用哪种缓存策略
     *
     * @param productInfo
     * @return
     */

    @CachePut(value = CACHE_NAME, key = "'key_'+#productInfo.getId()")
    @Override
    public ProductInfo saveLocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    /**
     * 从本地缓存中获取商品信息
     *
     * @param id
     * @return
     */
    @Cacheable(value = CACHE_NAME, key = "'key_'+#id")
    @Override
    public ProductInfo getLocalCache(Long id) {
        //没有取到则返回null, 取到了会自动从缓存中返回
        return null;
    }

    /**
     * 将商品信息保存到本地的ehcache缓存中
     *
     * @param productInfo
     */
    @Override
    @CachePut(value = CACHE_NAME, key = "'product_info_'+#productInfo.getId()")
    public ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    /**
     * 从本地ehcache缓存中获取商品信息
     *
     * @param productId
     * @return
     */
    @Override
    @Cacheable(value = CACHE_NAME, key = "'product_info_'+#productId")
    public ProductInfo getProductInfoFromLocalCache(Long productId) {
        return null;
    }

    /**
     * 将店铺信息保存到本地的ehcache缓存中
     *
     * @param shopInfo
     */
    @Override
    @CachePut(value = CACHE_NAME, key = "'shop_info_'+#shopInfo.getId()")
    public ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo) {
        return shopInfo;
    }

    /**
     * 从本地ehcache缓存中获取店铺信息
     *
     * @param shopId
     * @return
     */
    @Cacheable(value = CACHE_NAME, key = "'shop_info_'+#shopId")
    @Override
    public ShopInfo getShopInfoFromLocalCache(Long shopId) {
        return null;
    }

    /**
     * 将商品信息保存到redis中
     *
     * @param productInfo
     */
    @Override
    public void saveProductInfo2ReidsCache(ProductInfo productInfo) throws JsonProcessingException {
        String key = "product_info_" + productInfo.getId();
        ObjectMapper mapper = new ObjectMapper();
        redisTemplate.opsForValue().set(key, mapper.writeValueAsString(productInfo));
/*        SaveProductInfo2ReidsCacheCommand command = new SaveProductInfo2ReidsCacheCommand(productInfo);
        command.execute();*/
    }

    /**
     * 将店铺信息保存到redis中
     *
     * @param shopInfo
     */
    @Override
    public void saveShopInfo2ReidsCache(ShopInfo shopInfo) throws JsonProcessingException {
        String key = "shop_info_" + shopInfo.getId();
        ObjectMapper mapper = new ObjectMapper();
        redisTemplate.opsForValue().set(key, mapper.writeValueAsString(shopInfo));
/*        SaveShopInfo2ReidsCacheCommand command = new SaveShopInfo2ReidsCacheCommand(shopInfo);
        command.execute();*/
    }

    /**
     * 从redis中获取商品信息
     * @param productId
     */
    @Override
    public ProductInfo getProductInfoFromReidsCache(Long productId) {
        String key = "product_info_" + productId;
        String json = redisTemplate.opsForValue().get(key);
        if(json != null) {
            return JSONObject.parseObject(json, ProductInfo.class);
        }
        return null;
/*        GetProductInfoFromReidsCacheCommand command = new GetProductInfoFromReidsCacheCommand(productId);
        return command.execute();*/
    }

    /**
     * 从redis中获取店铺信息
     * @param shopId
     */
    @Override
    public ShopInfo getShopInfoFromReidsCache(Long shopId) {
        String key = "shop_info_" + shopId;
        String json = redisTemplate.opsForValue().get(key);
        if(json != null) {
            return JSONObject.parseObject(json, ShopInfo.class);
        }
        return null;
/*        GetShopInfoFromReidsCacheCommand command = new GetShopInfoFromReidsCacheCommand(shopId);
        return command.execute();*/
    }
}
