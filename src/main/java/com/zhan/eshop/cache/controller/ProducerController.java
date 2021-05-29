package com.zhan.eshop.cache.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhan.eshop.cache.model.KafkaMessageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ProducerController {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.eshop.cacheTopic}")
    private String topic;

    /**
     * 生产者向kafka中的topic发送消息测试
     *
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/sendProduct")
    public String sendProductMsg() throws JsonProcessingException {
        KafkaMessageModel kafkaMessage = new KafkaMessageModel();
        kafkaMessage.setId(5L);
        kafkaMessage.setServiceId("productInfoService");

        // 使用kafka模板发送信息
        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaTemplate.send(topic, kafkaMessage);
        String res = "消息:【" + kafkaMessage.toString() + "】发送成功 SUCCESS !";
        log.info(res);

        //***************发送成功或者失败后回调函数，可以做一些事情*******************
        // 发送成功回调
        SuccessCallback<SendResult<String, Object>> successCallback = new SuccessCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                //TODO 成功业务逻辑
                System.out.println("=================发送成功回调：onSuccess");
            }
        };
        // 发送失败回调
        FailureCallback failureCallback = new FailureCallback() {
            @Override
            public void onFailure(Throwable ex) {
                //TODO 失败业务逻辑
                System.out.println("=================发送失败回调：onFailure");
            }
        };

        listenableFuture.addCallback(successCallback, failureCallback);

        //***************发送成功或者失败后回调，可以做一些事情*******************

        return res;
    }

    /**
     * 生产者向kafka中的topic发送消息测试
     *
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/sendShop")
    public String sendShopMsg() throws JsonProcessingException {
        KafkaMessageModel kafkaMessage = new KafkaMessageModel();
        kafkaMessage.setId(1L);
        kafkaMessage.setServiceId("shopInfoService");

        // 使用kafka模板发送信息
        kafkaTemplate.send(topic, kafkaMessage);
        String res = "消息:【" + kafkaMessage.toString() + "】发送成功 SUCCESS !";
        log.info(res);
        return res;
    }
}
