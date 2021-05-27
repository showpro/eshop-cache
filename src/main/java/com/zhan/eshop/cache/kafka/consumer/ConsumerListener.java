//package com.zhan.eshop.cache.kafka.consumer;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Component;
//
///**
// * @Description 消费者监听消息并消费 kafka 消息测试
// *
// * 方案2：使用@KafkaListener注解，并设置topic,支持监听多个topic的消息,支持SPEL表达式。这样方便拆分多个不同topic处理不同业务逻辑。（特别是有自己的事务的时候，尤其方便）
// *  @KafkaListener(topics = "${templar.aggrement.agreementWithhold.topic}")
// *
// * @Author zhanzhan
// * @Date 2021/5/27 11:17
// */
//@Component
//@Slf4j
//public class ConsumerListener {
//
//    //建议看一下KafkaListener的源码 很多api 我们也可以指定分区消费消息
//    // topicPartitions ={@TopicPartition(topic = "topic1", partitions = { "0", "1" })}
//    @KafkaListener(topics = {"test_topic"}, groupId = "eshop-cache-group")  // 支持监听多个topic的消息
//    public void consumerMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) {
//        try {
//            String value = consumerRecord.value();
//            log.info("监听到的消息为：{}", value);
//            // TODO 接着可以进行消费业务处理......
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            //手动提交offset
//            ack.acknowledge();
//            log.info("消费结束");
//        }
//    }
//
//}
