package com.dhy.chat.exception;

import com.dhy.chat.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

/**
 * @author vghosthunter
 */
@RestControllerAdvice
public class RunTimeExceptionHandler {

    private Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result exceptionHandler(Exception e){
        e.printStackTrace();
        return Result.failure();
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result exceptionHandler(BusinessException e){
        e.printStackTrace();
        return Result.failure(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result exceptionHandler(EntityNotFoundException e){
        e.printStackTrace();
        log.error(e.getMessage());
        return Result.failure(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result exceptionHandler(NoSuchElementException e){
        e.printStackTrace();
        log.error(e.getMessage());
        return Result.failure();
    }

}
