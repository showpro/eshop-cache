package com.zhan.eshop.cache.kafka;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhan.eshop.cache.model.KafkaMessageModel;
import com.zhan.eshop.cache.model.ProductInfo;
import com.zhan.eshop.cache.model.ShopInfo;
import com.zhan.eshop.cache.service.CacheService;
import com.zhan.eshop.cache.zk.ZooKeeperSession;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanzhan
 * @date 2021/5/27 21:20
 */
@Component
@Slf4j
public class ConsumerListener {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private CacheService cacheService;

    //建议看一下KafkaListener的源码 很多api 我们也可以指定分区消费消息
    // topicPartitions ={@TopicPartition(topic = "topic1", partitions = { "0", "1" })}
    @KafkaListener(topics = {"eshop-cache"}, groupId = "eshop-cache-group")
    public void listen(KafkaMessageModel kafkaMessage, Acknowledgment ack) throws JsonProcessingException {
        log.info("消息内容: " + kafkaMessage.toString());

        // 从这里提取出消息对应的服务的标识
        String serviceId = kafkaMessage.getServiceId();

        /********* 如果是商品信息服务 *********/
        if ("productInfoService".equals(serviceId)) {
            // 处理商品信息变更的消息
            processProductInfoChangeMessage(kafkaMessage);
            /********** 如果是店铺信息服务 ********/
        } else if ("shopInfoService".equals(serviceId)) {
            // 处理店铺信息变更的消息
            processShopInfoChangeMessage(kafkaMessage);
        }

        //手动提交offset
        ack.acknowledge();
        log.info("消费结束");

    }

    /**
     * 处理商品信息变更的消息
     *
     * @param kafkaMessage
     */
    private void processProductInfoChangeMessage(KafkaMessageModel kafkaMessage) throws JsonProcessingException {
        // 提取出商品id
        Long productId = kafkaMessage.getId();

        /**
         * 调用商品信息服务的接口:
         * 直接用注释模拟：getProductInfo?productId=1，传递过去
         * 商品信息服务，一般来说就会去查询数据库，去获取productId=1的商品信息，然后返回回来
         */

        // 龙果有分布式事务的课程，主要讲解的分布式事务几种解决方案，里面也涉及到了一些mq，或者其他的一些技术，但是那些技术都是浅浅的给你搭建一下，使用
        // 你从一个课程里，还是学到的是里面围绕的讲解的一些核心的知识
        // 缓存架构：高并发、高性能、海量数据，等场景

        //假设去查询了数据库，获取到了productId=1的商品信息
        String productInfoJSON
            = "{\"id\": 6, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1, \"modifiedTime\": \"2021-06-03 12:00:00\"}";
        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);

        // 加代码，在将数据直接写入redis缓存之前，应该先获取一个zk的分布式锁
        ZooKeeperSession zkSession = ZooKeeperSession.getInstance();
        zkSession.acquireDistributedLock(productId);

        // 代码走到这里，说明获取到了锁
        // 先从redis中获取数据
        ProductInfo existedProductInfo = cacheService.getProductInfoFromReidsCache(productId);

        if (existedProductInfo != null) {
            // 比较当前数据的时间版本比已有数据的时间版本是新还是旧
            try {
                Date date = sdf.parse(productInfo.getModifiedTime());
                Date existedDate = sdf.parse(existedProductInfo.getModifiedTime());

                if (date.before(existedDate)) {
                    System.out.println("current date[" + productInfo.getModifiedTime() + "] is before existed date["
                        + existedProductInfo.getModifiedTime() + "]");
                    // 旧，则结束；新，则往下走，更新缓存进行覆盖
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("current date[" + productInfo.getModifiedTime() + "] is after existed date["
                + existedProductInfo.getModifiedTime() + "]");
        } else {
            System.out.println("existed product info is null......");
        }

        try {
            // 休眠10秒
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * ehcache 和 redis各存一份
         */
        cacheService.saveProductInfo2LocalCache(productInfo);
        System.out.println(
            "===================获取刚保存到本地缓存的商品信息：" + cacheService.getProductInfoFromLocalCache(productId));
        cacheService.saveProductInfo2ReidsCache(productInfo);
        // 释放分布式锁
        zkSession.releaseDistributedLock(productId);
    }

    /**
     * 处理店铺信息变更的消息
     *
     * @param kafkaMessage
     */
    @SuppressWarnings("unused")
    private void processShopInfoChangeMessage(KafkaMessageModel kafkaMessage) throws JsonProcessingException {
        // 提取出店铺id
        Long shopId = kafkaMessage.getId();

        // 调用商品信息服务的接口
        // 直接用注释模拟：getShopInfo?shopId=1，传递过去
        // 店铺信息服务，一般来说就会去查询数据库，去获取shopId=1的店铺信息，然后返回回来

        String shopInfoJSON = "{\"id\": 1, \"name\": \"小王的手机店\", \"level\": 5, \"goodCommentRate\":0.99}";
        ShopInfo shopInfo = JSONObject.parseObject(shopInfoJSON, ShopInfo.class);
        //保存到本地缓存
        cacheService.saveShopInfo2LocalCache(shopInfo);
        System.out.println("===================获取刚保存到本地缓存的店铺信息：" + cacheService.getShopInfoFromLocalCache(shopId));
        //保存到redis分布式缓存
        cacheService.saveShopInfo2ReidsCache(shopInfo);
    }

}
