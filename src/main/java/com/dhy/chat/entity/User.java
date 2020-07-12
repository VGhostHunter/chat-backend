package com.dhy.chat.entity;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "user", indexes = {
    @Index(name = "idx_username",columnList = "username", unique = true)
})
@DynamicUpdate()
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class User extends CreateAndUpdateAuditEntity implements UserDetails {

    private static final long serialVersionUID = 1284011010357285456L;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id")
    private String id;

    private String username;

    private String mobile;

    private String password;

    private String picture;

    private boolean isExpired;

    private boolean isEnabled = true;

    private boolean locked;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<UserAuthority> userAuthorities = new ArrayList<>();

    /**
     * 账户是否过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return ! isExpired;
    }

    /**
     * 是否被锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return ! locked;
    }

    /**
     * 密码是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否允许登陆
     */
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * 权限
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userAuthorities.stream()
                .map(UserAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public void setAuthorities(List<GrantedAuthority> grantedAuthority) {
        this.userAuthorities = grantedAuthority.stream().map(x -> {
            UserAuthority userAuthority = new UserAuthority();
            userAuthority.setUser(this);
            if(x instanceof Authority) {
                Authority authority = (Authority)x;
                userAuthority.setAuthority(authority);
            }
            return userAuthority;
        }).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public List<UserAuthority> getUserAuthorities() {
        return userAuthorities;
    }

    public void setUserAuthorities(List<UserAuthority> userAuthorities) {
        this.userAuthorities = userAuthorities;
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
}