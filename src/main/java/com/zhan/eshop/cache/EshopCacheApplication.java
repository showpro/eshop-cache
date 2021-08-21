package com.zhan.eshop.cache;

//import com.zhan.eshop.cache.service.MqProducerService;
import java.util.HashSet;
import java.util.Set;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * 缓存启动入口
 *
 * @author zhanzhan
 */
@SpringBootApplication
// mapper 接口类扫描包配置
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

    @Bean
    public JedisCluster JedisClusterFactory() {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("192.168.133.133", 7001));
        jedisClusterNodes.add(new HostAndPort("192.168.133.133", 7002));
        jedisClusterNodes.add(new HostAndPort("192.168.133.128", 7003));
        jedisClusterNodes.add(new HostAndPort("192.168.133.128", 7004));
        jedisClusterNodes.add(new HostAndPort("192.168.133.129", 7005));
        jedisClusterNodes.add(new HostAndPort("192.168.133.129", 7006));
        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes);
        return jedisCluster;
    }

    public static void main(String[] args) {
        SpringApplication.run(EshopCacheApplication.class, args);
    }

}
