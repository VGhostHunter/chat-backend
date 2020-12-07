package com.dhy.chat.web.filter;

import com.dhy.chat.entity.Authority;
import com.dhy.chat.entity.User;
import com.dhy.chat.web.config.properties.JwtProperties;
import com.gexin.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author vghosthunter
 */
@Component
public class JwtAuthenticationTokenFilter extends BasicAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JwtProperties jwtProperties;

    public JwtAuthenticationTokenFilter(@Lazy AuthenticationManager authenticationManager,
                                        JwtProperties jwtProperties) {
        super(authenticationManager);
        this.jwtProperties = jwtProperties;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求头中JWT的Token
        String requestHeader = request.getHeader(jwtProperties.getTokenHeader());
        if (null != requestHeader && requestHeader.startsWith(jwtProperties.getTokenPrefix())) {
            try {
                // 截取JWT前缀
                String token = requestHeader.replace(jwtProperties.getTokenPrefix(), "");
                // 解析JWT
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtProperties.getSignKey())
                        .parseClaimsJws(token)
                        .getBody();
                // 获取用户名
                String username = claims.getSubject();
                String userId = claims.getId();
                if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(userId)) {
                    // 获取角色
                    Set<Authority> authorities = new HashSet<>();
                    String authority = claims.get("authorities").toString();
                    if(!StringUtils.isEmpty(authority)){
                        List<String> authorityMap = JSONObject.parseObject(authority, List.class);
                        for(String role : authorityMap){
                            if(!StringUtils.isEmpty(role)) {
                                Authority auth = new Authority();
                                auth.setAuthority(role);
                                authorities.add(auth);
                            }
                        }
                    }
                    //组装参数
                    User user = new User();
                    user.setUsername(claims.getSubject());
                    user.setId(claims.getId());
                    user.setAuthorities(authorities);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, userId, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e){
                log.info("Token过期");
            } catch (Exception e) {
                log.info("Token无效");
            }
        }
        filterChain.doFilter(request, response);
    }
}