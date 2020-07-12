package com.dhy.chat.dto;

import org.springframework.http.HttpStatus;

/**
 * @author vghosthunter
 */
public class Result<T> {

    private String message;

    private int code;

    private T data;

    public Result(HttpStatus status) {
        this.code = status.value();
        this.message = status.getReasonPhrase();
    }

    public Result(HttpStatus status, T data) {
        this(status);
        this.data = data;
    }

    public Result(int code, String message){
        this.code = code;
        this.message = message;
    }

    public static <T> Result<T> succeeded() {
        return new Result<>(HttpStatus.OK);
    }

    public static <T> Result<T> failure(HttpStatus httpStatus, String message) {
        return new Result(httpStatus.value(), message);
    }

    public static <T> Result<T> failure(String message) {
        return new Result(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static <T> Result<T> failure() {
        return new Result<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> Result<T> succeeded(T data) {
        Result<T> result = new Result<>(HttpStatus.OK);
        result.setData(data);
        return result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}