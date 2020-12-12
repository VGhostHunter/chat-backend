package com.dhy.chat.web.config;

import com.dhy.chat.web.config.properties.AppProperties;
import com.sendgrid.SendGrid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vghosthunter
 */
@Configuration
public class EmailConfig {

    private final AppProperties appProperties;

    public EmailConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.provider.email-provider", name = "name", havingValue = "api")
    public SendGrid sendGrid() {
        return new SendGrid(appProperties.getProvider().getApiKey());
    }
}
