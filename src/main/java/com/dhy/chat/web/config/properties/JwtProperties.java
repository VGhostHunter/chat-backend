package com.dhy.chat.web.config.properties;

/**
 * @author vghosthunter
 */
public class JwtProperties {

    private String key;

    private String refreshKey;

    private String tokenHeader = "Authorization";

    private String tokenPrefix = "Bearer ";

    private long accessTokenExpireTime = 3600000;

    private long refreshTokenExpireTime = 2592000000L;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRefreshKey() {
        return refreshKey;
    }

    public void setRefreshKey(String refreshKey) {
        this.refreshKey = refreshKey;
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public long getAccessTokenExpireTime() {
        return accessTokenExpireTime;
    }

    public void setAccessTokenExpireTime(long accessTokenExpireTime) {
        this.accessTokenExpireTime = accessTokenExpireTime;
    }

    public long getRefreshTokenExpireTime() {
        return refreshTokenExpireTime;
    }

    public void setRefreshTokenExpireTime(long refreshTokenExpireTime) {
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }
}
