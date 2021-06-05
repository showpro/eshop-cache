/**
 * yingyinglicai.com Inc.
 * Copyright (c) 2013-2017 All Rights Reserved.
 */
package com.zhan.eshop.cache.spring;

import java.util.Objects;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring 上下文
 *
 * @author Mr.Zhan
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public static <T> T getBeanByName(String beanName) {
        if (applicationContext == null) {
            return null;
        }
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        if (Objects.isNull(applicationContext)) {
            throw new RuntimeException("applicationContext注入失败！");
        }
        return applicationContext;
    }
}
