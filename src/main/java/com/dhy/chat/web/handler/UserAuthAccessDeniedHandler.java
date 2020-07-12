package com.dhy.chat.web.handler;

import com.dhy.chat.dto.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 暂无权限处理类
 * @author vghosthunter
 */
@Component
public class UserAuthAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public UserAuthAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 暂无权限返回结果
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(objectMapper.writeValueAsString(Result.failure(HttpStatus.FORBIDDEN, "暂无权限")));
    }
}