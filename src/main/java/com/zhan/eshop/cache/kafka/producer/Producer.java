//package com.zhan.eshop.cache.kafka.producer;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
//
///**
// * @Description 生产者发送消息到topic
// * @Author zhanzhan
// * @Date 2021/5/27 13:39
// */
//public class Producer {
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @Value("${kafka.eshop.topic}")
//    private String topic;
//
//    public void sendMessage(){
//        kafkaTemplate.send(topic,"kafkaf发送消息测试");
//    }
//}
