package org.quickjedis.utils;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

import java.math.BigDecimal;

/**
 * 对象映射器:基于jackson将Java对象转为json，或者将json转为Java对象
 * 将JSON解析为Java对象的过程称为 [从JSON反序列化Java对象]
 * 从Java对象生成JSON的过程称为 [序列化Java对象到JSON]
 */
public class JacksonObjectMapper extends ObjectMapper {
    public JacksonObjectMapper() {
        super();
        //注册序列化器，使用自定义序列化类MyToStringSerializer，通过转为String类型，禁止BigDecimal类型使用科学计数法
        SimpleModule simpleModule = new SimpleModule("CustomSerializer", new Version(1, 0, 0, null))
                .addSerializer(BigDecimal.class, MyToStringSerializer.instance);
        //注册功能模块 例如，可以添加自定义序列化器和反序列化器
        this.registerModule(simpleModule);
    }
}