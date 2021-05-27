package com.zhan.eshop.cache.serializable;

import com.zhan.eshop.cache.model.KafkaMessageModel;

import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * 创建 KafkaMessageModel 反序列化器
 * @author john
 * @date 2020/11/5 - 17:02
 */
public class KafkaMessageDeserializer implements Deserializer<KafkaMessageModel> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public KafkaMessageModel deserialize(String topic, byte[] bytes) {
        KafkaMessageModel kafkaMessage = null;
        ByteArrayInputStream inputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            inputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(inputStream);
            kafkaMessage = (KafkaMessageModel) objectInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return kafkaMessage;
    }

    @Override
    public void close() {

    }
}
