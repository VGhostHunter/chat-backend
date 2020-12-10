package com.dhy.chat.dto.user;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author vghosthunter
 */
@ApiModel("CreateUserDto")
public class CreateUserDto {

    @NotNull
    @NotBlank
    @Size(min = 4, max = 50)
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 50)
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
