package com.zhan.eshop.cache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 定义一个用来发送消息的载体
 * @Author zhanzhan
 * @Date 2021/5/27 10:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessageModel {
    private String serviceId;
    private Long Id;
}
