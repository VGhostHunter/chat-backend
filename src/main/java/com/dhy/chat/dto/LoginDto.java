package com.dhy.chat.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author vghosthunter
 */
public class LoginDto {

    @NotNull
    @NotBlank
    @Size(min = 4, max = 50)
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 50)
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
