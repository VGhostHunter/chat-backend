package com.dhy.chat.web.config;

import com.dhy.chat.utils.IdGenerator;
import com.dhy.chat.utils.SnowflakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfig {

    @Bean
    public IdGenerator idGenerator() {
        var idGenerator = new SnowflakeIdGenerator();
        idGenerator.setWorkerId(0);
        idGenerator.setDataCenterId(0);
        return idGenerator;
    }
}
