package com.dhy.chat.web.filter;

import com.dhy.chat.entity.Authority;
import com.dhy.chat.entity.User;
import com.dhy.chat.utils.CollectionUtil;
import com.dhy.chat.utils.JwtTokenUtil;
import com.dhy.chat.web.config.properties.AppProperties;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.stream.Collectors.toSet;

/**
 * @author vghosthunter
 */
@Component
public class JwtAuthenticationTokenFilter extends BasicAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AppProperties appProperties;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationTokenFilter(@Lazy AuthenticationManager authenticationManager,
                                        AppProperties appProperties,
                                        JwtTokenUtil jwtTokenUtil) {
        super(authenticationManager);
        this.appProperties = appProperties;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (checkJwtToken(request)) {
            var requestHeader = request.getHeader(appProperties.getJwt().getTokenHeader());
            String token = requestHeader.replace(appProperties.getJwt().getTokenPrefix(), "");

            jwtTokenUtil.parseClaims(token, jwtTokenUtil.getKey())
                .filter(claims -> claims.get("authorities") != null)
                .ifPresentOrElse(
                    this::setupSpringAuthentication,
                    SecurityContextHolder::clearContext
                );
        }
        filterChain.doFilter(request, response);
    }

    private void setupSpringAuthentication(Claims claims) {
        var rawList = CollectionUtil.convertObjectToList(claims.get("authorities"));
        var authorities = rawList.stream()
                .map(String::valueOf)
                .map(Authority::new)
                .collect(toSet());

        //组装参数
        User user = new User();
        user.setUsername(claims.getSubject());
        user.setId(claims.getId());
        user.setAuthorities(authorities);

        var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    /**
     * 检查 JWT Token 是否在 HTTP 报头中
     *
     * @param req HTTP 请求
     * @return 是否有 JWT Token
     */
    private boolean checkJwtToken(HttpServletRequest req) {
        String authenticationHeader = req.getHeader(appProperties.getJwt().getTokenHeader());
        return authenticationHeader != null && authenticationHeader.startsWith(appProperties.getJwt().getTokenPrefix());
    }
}