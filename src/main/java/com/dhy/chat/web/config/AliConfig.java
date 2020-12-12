package com.dhy.chat.web.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.dhy.chat.web.config.properties.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vghosthunter
 */
@Configuration
public class AliConfig {

    private final AppProperties appProperties;

    public AliConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public IAcsClient acsClient() {
        DefaultProfile profile = DefaultProfile.getProfile(
                appProperties.getAliSmsProperties().getRegionId(),
                appProperties.getAliSmsProperties().getApiKey(),
                appProperties.getAliSmsProperties().getApiSecret());
        return new DefaultAcsClient(profile);
    }
}
