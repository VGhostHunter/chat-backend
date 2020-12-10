package com.dhy.chat.utils;

import com.dhy.chat.entity.User;
import com.dhy.chat.web.config.properties.AppProperties;
import io.jsonwebtoken.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author vghosthunter
 */
@Component
public class JwtTokenUtil {

    /**
     * 用于签名 Access Token
     */
    private final Key key;
    /**
     * 用于签名 Refresh Token
     */
    private final Key refreshKey;

    private final AppProperties appProperties;

    public JwtTokenUtil(AppProperties appProperties) {
        this.appProperties = appProperties;
        key = new SecretKeySpec(Base64.getDecoder().decode(appProperties.getJwt().getKey()), "HmacSHA512");
        refreshKey = new SecretKeySpec(Base64.getDecoder().decode(appProperties.getJwt().getRefreshKey()), "HmacSHA512");
    }

    public String createAccessToken(User user) {
        return createJWTToken(user, appProperties.getJwt().getAccessTokenExpireTime(), key);
    }

    public String createRefreshToken(User user) {
        return createJWTToken(user, appProperties.getJwt().getRefreshTokenExpireTime(), refreshKey);
    }

    /**
     * 根据用户信息生成一个 JWT
     *
     * @param user  用户信息
     * @param timeToExpire 毫秒单位的失效时间
     * @param signKey      签名使用的 key
     * @return JWT
     */
    private String createJWTToken(User user, long timeToExpire, Key signKey) {
        var now = System.currentTimeMillis();
        return Jwts
                .builder()
                .setId(user.getId())
                .setSubject(user.getUsername())
                .claim("authorities",
                        user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + timeToExpire))
                .signWith(signKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateAccessToken(String jwtToken) {
        return validateToken(jwtToken, key);
    }

    public boolean validateRefreshToken(String jwtToken) {
        return validateToken(jwtToken, refreshKey);
    }

    public boolean validateToken(String jwtToken, Key signKey) {
        try {
            Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(jwtToken);
            return true;
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String buildAccessTokenWithRefreshToken(String refreshToken) {
        var now = System.currentTimeMillis();
        return parseClaims(refreshToken, refreshKey)
                .map(claims -> Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(new Date(now))
                        .setExpiration(new Date(now + appProperties.getJwt().getAccessTokenExpireTime()))
                        .signWith(key, SignatureAlgorithm.HS512).compact())
                .orElseThrow();
    }

    public Optional<Claims> parseClaims(String jwtToken, Key signKey) {
        try {
            var claims = Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(jwtToken).getBody();
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean validateWithoutExpiration(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken);
            return true;
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            if (e instanceof ExpiredJwtException) {
                return true;
            }
        }
        return false;
    }

    public Key getKey() {
        return key;
    }

    public Key getRefreshKey() {
        return refreshKey;
    }
}
