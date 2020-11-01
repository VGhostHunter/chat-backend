package com.dhy.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.SimpleDateFormat;

/**
 * @author vghosthunter
 * spring 团队建议：“在bean中始终使用基于构造函数的依赖注入，始终使用断言来强制依赖”。
 *
 * 在创建对象的时候，强制依赖别的对象，确保创建对象时每个对象都是有效状态。
 * 构造器中可以添加对象初始化的校验逻辑。
 * 可以清楚的区分对象是通过setter方法注入的（非final对象）还是通过强制依赖注入的（final对象）
 */
@EnableMongoAuditing
@EnableJpaAuditing
@SpringBootApplication
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        //序列化的时候序列对象的所有属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //如果是空对象的时候,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        return objectMapper;
    }
}
