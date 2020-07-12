package com.dhy.chat.dto;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotBlank;

@ApiModel("CreateUserDto")
public class CreateUserDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String mobile;

    private String picture;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
