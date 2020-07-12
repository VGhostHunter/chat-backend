package com.dhy.chat.exception;

import com.dhy.chat.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author vghosthunter
 */
@RestControllerAdvice
public class BindingExceptionHandler {

    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 用来处理没有加@RequestBody的binding
     * @param e e
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result validExceptionHandler(BindException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(ex -> ex.getField() + ": " + ex.getDefaultMessage())
                .reduce((s1, s2) -> s1 + "; " + s2).get();
        log.info(msg);
        return Result.failure(HttpStatus.BAD_REQUEST, msg);
    }

    /**
     * 用来处理加了@RequestBody的binding
     * @param e e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result validExceptionHandler(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(ex -> ex.getField() + ": " + ex.getDefaultMessage())
                .reduce((s1, s2) -> s1 + "; " + s2).get();
        log.info(msg);
        return Result.failure(HttpStatus.BAD_REQUEST, msg);
    }
}
