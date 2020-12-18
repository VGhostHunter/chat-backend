package com.dhy.chat.web.handler;

import com.dhy.chat.dto.Result;
import com.dhy.chat.utils.LocalMessageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author vghosthunter
 */
@Component
public class UserAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final LocalMessageUtil localMessageUtil;

    public UserAuthenticationEntryPointHandler(ObjectMapper objectMapper,
                                               LocalMessageUtil localMessageUtil) {
        this.objectMapper = objectMapper;
        this.localMessageUtil = localMessageUtil;
    }

    /**
     * 用户未登录返回结果
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(Result.failure(HttpStatus.UNAUTHORIZED, localMessageUtil.GetMsg("message.userNotLogin"))));
    }
}