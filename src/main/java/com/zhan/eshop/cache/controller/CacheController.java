package com.zhan.eshop.cache.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zhan.eshop.cache.model.ProductInfo;
import com.zhan.eshop.cache.model.ShopInfo;
import com.zhan.eshop.cache.rebuild.RebuildCacheQueue;
import com.zhan.eshop.cache.service.CacheService;

/**
 * 缓存Controller
 *
 * @author zhanzhan
 * @date 2021/5/23 20:34
 */
@Controller
public class CacheController {

    @Resource
    private CacheService cacheService;

    /**
     * 测试放入本地缓存
     * http://localhost:8080/testPutCache?id=1&name=test_product&price=55.50
     *
     * @param productInfo
     * @return
     */
    @RequestMapping("/testPutCache")
    @ResponseBody
    public String testPutCache(ProductInfo productInfo) {
        cacheService.saveLocalCache(productInfo);
        return "success";
    }

    /**
     * 测试从本地缓存中获取
     * http://localhost:8080/testGetCache?id=1
     *
     * @param id
     * @return
     */
    @RequestMapping("/testGetCache")
    @ResponseBody
    public ProductInfo testGetCache(Long id) {
        return cacheService.getLocalCache(id);
    }

    /**
     * 获取商品信息
     *
     * @param productId
     * @return
     */
    @RequestMapping("/getProductInfo")
    @ResponseBody
    public ProductInfo getProductInfo(Long productId) {
        ProductInfo productInfo = null;

        // 1、从redis缓存中获取
        productInfo = cacheService.getProductInfoFromReidsCache(productId);
        if(productInfo != null) {
            System.out.println("=================从redis中获取缓存，商品信息=" + productInfo);
        }

        if(productInfo == null) {
            // 2、从本地堆缓存获取
            productInfo = cacheService.getProductInfoFromLocalCache(productId);
            if(productInfo != null) {
                System.out.println("=================从ehcache中获取缓存，商品信息=" + productInfo);
            }
        }

        if(productInfo == null) {
            // 3、就需要从数据源重新拉去数据，重建缓存，但是这里先不讲
/*            GetProductInfoCommand command = new GetProductInfoCommand(productId);
            productInfo = command.execute();*/

            // 将数据推送到一个内存队列中
            String productInfoJSON
                = "{\"id\": 6, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1, \"modifiedTime\": \"2021-06-03 12:01:11\"}";
            productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);

            RebuildCacheQueue rebuildCacheQueue = RebuildCacheQueue.getInstance();
            rebuildCacheQueue.putProductInfo(productInfo);
        }

        return productInfo;
    }

    /**
     * 获取店铺信息
     *
     * @param shopId
     * @return
     */
    @RequestMapping("/getShopInfo")
    @ResponseBody
    public ShopInfo getShopInfo(Long shopId) {
        ShopInfo shopInfo = null;

        shopInfo = cacheService.getShopInfoFromReidsCache(shopId);
        System.out.println("=================从redis中获取缓存，店铺信息=" + shopInfo);

        if(shopInfo == null) {
            shopInfo = cacheService.getShopInfoFromLocalCache(shopId);
            System.out.println("=================从ehcache中获取缓存，店铺信息=" + shopInfo);
        }

        if(shopInfo == null) {
            // 就需要从数据源重新拉去数据，重建缓存，但是这里先不讲
        }

        return shopInfo;
    }
}
