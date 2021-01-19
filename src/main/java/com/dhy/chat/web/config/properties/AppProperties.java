package com.dhy.chat.web.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author vghosthunter
 */
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private JwtProperties jwt = new JwtProperties();

    private AliSmsProperties aliSmsProperties = new AliSmsProperties();

    private MinioProperties minio = new MinioProperties();

    private ProviderProperties provider = new ProviderProperties();

    private LeanCloudProperties leanCloud = new LeanCloudProperties();

    public JwtProperties getJwt() {
        return jwt;
    }

    public void setJwt(JwtProperties jwt) {
        this.jwt = jwt;
    }

    public ProviderProperties getProvider() {
        return provider;
    }

    public void setProvider(ProviderProperties provider) {
        this.provider = provider;
    }

    public AliSmsProperties getAliSmsProperties() {
        return aliSmsProperties;
    }

    public void setAliSmsProperties(AliSmsProperties aliSmsProperties) {
        this.aliSmsProperties = aliSmsProperties;
    }

    public LeanCloudProperties getLeanCloud() {
        return leanCloud;
    }

    public void setLeanCloud(LeanCloudProperties leanCloud) {
        this.leanCloud = leanCloud;
    }

    public MinioProperties getMinio() {
        return minio;
    }

    public void setMinio(MinioProperties minio) {
        this.minio = minio;
    }
}
