package com.dhy.chat.web.handler;

import com.dhy.chat.entity.User;
import com.dhy.chat.utils.JwtTokenUtil;
import com.dhy.chat.web.config.properties.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vghosthunter
 */
@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtProperties jwtProperties;

    public UserLoginSuccessHandler(ObjectMapper objectMapper,
                                   JwtTokenUtil jwtTokenUtil,
                                   JwtProperties jwtProperties) {
        this.objectMapper = objectMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtProperties = jwtProperties;
    }

    /**
     * 登录成功返回结果
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 组装JWT
        User user =  (User) authentication.getPrincipal();
        String token = jwtTokenUtil.createAccessToken(user);
        token = jwtProperties.getTokenPrefix() + token;

        // 封装返回参数
        Map<String,Object> resultData = new HashMap<>();
        resultData.put("code","200");
        resultData.put("msg", "登录成功");
        resultData.put("token",token);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(resultData));
    }
}