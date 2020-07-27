package com.dhy.chat.utils;

import com.dhy.chat.entity.User;
import com.dhy.chat.web.config.properties.JwtProperties;
import com.gexin.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author vghosthunter
 */
@Component
public class JwtTokenUtil {

    private final JwtProperties jwtProperties;

    public JwtTokenUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * 生成Token
     */
    public String createAccessToken(User user){
        // 登陆成功生成JWT
        String token = Jwts.builder()
                // 放入用户名和用户ID
                .setId(user.getId())
                // 主题
                .setSubject(user.getUsername())
                // 签发时间
                .setIssuedAt(new Date())
                // 签发者
                .setIssuer(jwtProperties.getIssuer())
                // 自定义属性 放入用户拥有权限
                .claim("authorities", JSON.toJSONString(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())))
                // 失效时间
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                // 签名算法和密钥
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSignKey())
                .compact();
        return token;
    }
}
