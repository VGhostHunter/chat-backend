package com.dhy.chat.utils;

import com.dhy.chat.entity.Authority;
import com.dhy.chat.entity.User;
import com.dhy.chat.web.config.properties.AppProperties;
import com.dhy.chat.web.config.properties.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;
    private AppProperties appProperties;

    @BeforeEach
    public void setup() {
        appProperties = new AppProperties();
        JwtProperties jwt = new JwtProperties();
        var key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        var refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        jwt.setKey(Base64.getEncoder().encodeToString(key.getEncoded()));
        jwt.setRefreshKey(Base64.getEncoder().encodeToString(refreshKey.getEncoded()));
        appProperties.setJwt(jwt);
        jwtTokenUtil = new JwtTokenUtil(appProperties);
    }

    @Test
    public void givenUserDetails_thenCreateTokenSuccess() {
        var username = "user";
        var auth1 = new Authority();
        auth1.setAuthority("ROLE_USER");
        var auth2 = new Authority();
        auth2.setAuthority("ROLE_ADMIN");
        var authorities = Set.of(
                auth1, auth2
        );
        var user = new User();
        user.setUsername(username);
        user.setAuthorities(authorities);

        // 创建 jwt
        var token = jwtTokenUtil.createAccessToken(user);
        // 解析
        var parsedClaims = Jwts.parserBuilder().setSigningKey(jwtTokenUtil.getKey()).build().parseClaimsJws(token).getBody();
        // subject 和 username 应该相同
        assertEquals(username, parsedClaims.getSubject());
    }
}