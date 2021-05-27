package com.zhan.eshop.cache.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhan.eshop.cache.model.ProductInfo;
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


}
