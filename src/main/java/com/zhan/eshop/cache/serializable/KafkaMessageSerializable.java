package com.zhan.eshop.cache.serializable;

import com.zhan.eshop.cache.model.KafkaMessageModel;

import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * 采用自定义序列化和反序列化器进行 【实体类】 的序列化和反序列化
 *
 * 创建KafkaMessageModel序列化器
 *
 * @author john
 * @date 2020/11/5 - 17:00
 */
public class KafkaMessageSerializable implements Serializer<KafkaMessageModel> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String topic, KafkaMessageModel kafkaMessage) {
        byte[] dataArray = null;
        ByteArrayOutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(kafkaMessage);
            dataArray = outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataArray;
    }

    @Override
    public void close() {

    }
}
