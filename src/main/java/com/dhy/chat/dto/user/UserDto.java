package com.dhy.chat.dto.user;

/**
 * @author vghosthunter
 */
public class UserDto {

    private String id;

    private String username;

    private String mobile;

    private String picture;

    private String email;

    private boolean usingMfa;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isUsingMfa() {
        return usingMfa;
    }

    public void setUsingMfa(boolean usingMfa) {
        this.usingMfa = usingMfa;
    }
}
