package com.zhan.eshop.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhan.eshop.cache.model.KafkaMessageModel;
import com.zhan.eshop.cache.zk.ZooKeeperSession;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
class EshopCacheApplicationTests {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    public void testKafka() throws JsonProcessingException {
        //建议看一下KafkaTemplate的源码 很多api 我们可以指定分区发送消息
        KafkaMessageModel kafkaMessage = new KafkaMessageModel();
        kafkaMessage.setId(1L);
        kafkaMessage.setServiceId("productInfoService");

        kafkaTemplate.send("test", kafkaMessage); //使用kafka模板发送信息
        String res = "消息:【" + kafkaMessage.toString() + "】发送成功 SUCCESS !";
        log.info(res);
    }

    @Test
    public void zooKeeperTest() {
        ZooKeeperSession zkSession = ZooKeeperSession.getInstance();
    }

}
