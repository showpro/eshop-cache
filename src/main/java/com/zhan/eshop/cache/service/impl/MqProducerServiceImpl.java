package com.zhan.eshop.cache.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhan.eshop.cache.service.MqProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author zhanzhan
 * @Date 2021/4/23 13:40
 */
@Slf4j
@Component
public class MqProducerServiceImpl implements MqProducerService {

    /**
     * RocketMQ发送消息模板
     */
    //@Autowired
    //private RocketMQTemplate rocketMqTemplate;

    /**
     * Kafka发送消息模板
     */
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 从配置文件中读取 topic
     */
    @Value("${kafka.eshop.cacheTopic}")
    private String topic;

    /**
     * 发送消息到 topic
     *
     * @param message
     * @return
     */
    @Override
    public void sendMessage(Object message) {
        try {
            log.info("start send: " + JSON.toJSONString(message));

            // 通过rocketMq发送消息
            //rocketMqTemplate.convertAndSend("kxw_direct_order_assign_topic", message);

            // 通过kafka发送消息
            kafkaTemplate.send(topic, JSON.toJSONString(message));
            log.info("end send: " + JSON.toJSONString(message));
        } catch (Exception e) {
            log.error("sendTopicMsg error", e);
        }
    }
}
