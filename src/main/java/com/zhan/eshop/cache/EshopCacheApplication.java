package com.zhan.eshop.cache;

//import com.zhan.eshop.cache.service.MqProducerService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PostConstruct;

/**
 * 缓存启动入口
 *
 * @author zhanzhan
 */
@SpringBootApplication
@MapperScan("com.zhan.eshop.cache.mapper")
@EnableCaching
public class EshopCacheApplication {

    /**
     * 模拟发送消息测试：系统启动的时候发送消息到 Topic
     */
//    @Autowired
//    private MqProducerService mqProducer;
//    @PostConstruct
//    public void init() {
//        for (int i = 0; i < 10; i++) {
//            //调用消息发送类中的消息发送方法
//            mqProducer.sendMessage(String.valueOf(i));
//        }
//    }


    public static void main(String[] args) {
        SpringApplication.run(EshopCacheApplication.class, args);
    }

}
