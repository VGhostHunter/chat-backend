package com.dhy.chat.exception;

/**
 * 业务异常
 * @author vghosthunter
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
