package com.zhan.eshop.cache.spring;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.zhan.eshop.cache.rebuild.RebuildCacheThread;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化，注册等
 *
 * @author zhanzhan
 * @date 2021/6/5 12:36
 */
@Component
@Slf4j
public class InitListener {

    @PostConstruct
    public void startRebuildCacheThread() {
        new Thread(new RebuildCacheThread()).start();
        log.info("服务启动，缓存重建线程启动成功！");
    }
}
