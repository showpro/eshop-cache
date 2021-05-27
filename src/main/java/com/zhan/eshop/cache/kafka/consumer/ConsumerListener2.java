//package com.zhan.eshop.cache.kafka.consumer;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.listener.MessageListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Component;
//
///**
// * @Description 消费者监听消息并消费 kafka 消息测试
// * <p>
// * 方案1:直接实现MessageListener接口，复写onMessage方法，实现自定义消费业务逻辑。
// *
// * @Author zhanzhan
// * @Date 2021/5/27 11:17
// */
//@Component
//@Slf4j
//public class ConsumerListener2 implements MessageListener<String, String> {
//    @Override
//    public void onMessage(ConsumerRecord<String, String> data) {
//        Object o = data.value();
//        System.out.println("---收到kafka发送的消息---");
//        System.out.println(String.valueOf(o));
//        //根据不同主题，消费
//        if ("test_topic".equals(data.topic())) {
//            //todo 逻辑1...
//        } else if ("主题2".equals(data.topic())) {
//            //todo 逻辑2...
//        }
//    }
//}
