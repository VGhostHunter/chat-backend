package com.dhy.chat.web.handler;

import com.dhy.chat.dto.Result;
import com.dhy.chat.utils.LocalMessageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author vghosthunter
 */
@Component
public class UserLoginFailureHandler implements AuthenticationFailureHandler {

    private Logger log = LoggerFactory.getLogger(getClass());

    private final ObjectMapper objectMapper;
    private final LocalMessageUtil localMessageUtil;

    public UserLoginFailureHandler(ObjectMapper objectMapper,
                                   LocalMessageUtil localMessageUtil) {
        this.objectMapper = objectMapper;
        this.localMessageUtil = localMessageUtil;
    }

    /**
     * 登录失败返回结果
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        // 这些对于操作的处理类可以根据不同异常进行不同处理
        if (exception instanceof UsernameNotFoundException){
            log.info("【登录失败】"+exception.getMessage());
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.getWriter().write(objectMapper.writeValueAsString(Result.failure(HttpStatus.NOT_FOUND, localMessageUtil.GetMsg("message.usernameNotExist"))));

        } else if (exception instanceof LockedException){
            log.info("【登录失败】"+exception.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(Result.failure(HttpStatus.INTERNAL_SERVER_ERROR, localMessageUtil.GetMsg("message.userIsLocked"))));
        } else if (exception instanceof BadCredentialsException){
            log.info("【登录失败】"+exception.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write(objectMapper.writeValueAsString(Result.failure(HttpStatus.INTERNAL_SERVER_ERROR, localMessageUtil.GetMsg("message.usernameOrPasswordError"))));
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(objectMapper.writeValueAsString(Result.failure(HttpStatus.INTERNAL_SERVER_ERROR, localMessageUtil.GetMsg("message.loginFailed"))));
        }
    }
}