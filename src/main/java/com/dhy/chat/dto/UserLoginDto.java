package com.dhy.chat.dto;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotBlank;

@ApiModel("UserLoginDto")
public class UserLoginDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
