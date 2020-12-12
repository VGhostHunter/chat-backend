package com.dhy.chat.web.config;

import cn.leancloud.AVLogger;
import cn.leancloud.core.AVOSCloud;
import com.dhy.chat.web.config.properties.AppProperties;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import javax.annotation.PostConstruct;

/**
 * @author vghosthunter
 */
public class LeanCloudConfig {

    private final AppProperties appProperties;
    private final Environment env;

    public LeanCloudConfig(AppProperties appProperties,
                           Environment env) {
        this.appProperties = appProperties;
        this.env = env;
    }

    @PostConstruct()
    public void initialize() {
        if (env.acceptsProfiles(Profiles.of("prod"))) {
            AVOSCloud.setLogLevel(AVLogger.Level.ERROR);
        } else {
            AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);
        }
        AVOSCloud.initialize(appProperties.getLeanCloud().getAppId(), appProperties.getLeanCloud().getAppKey());
    }
}
