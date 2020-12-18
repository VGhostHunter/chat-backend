package com.dhy.chat.web.config;

import com.dhy.chat.web.config.properties.AppProperties;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    private final AppProperties appProperties;

    public MinioConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }


    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(appProperties.getMinio().getEndPoint())
                .credentials(appProperties.getMinio().getAccessKey(), appProperties.getMinio().getSecretKey())
                .build();
    }
}
