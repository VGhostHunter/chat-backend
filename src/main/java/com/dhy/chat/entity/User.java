package com.dhy.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@DynamicUpdate()
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class User extends CreateAndUpdateAuditEntity implements UserDetails {

    private static final long serialVersionUID = 1284011010357285456L;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id")
    private String id;

    @Column(unique = true)
    private String username;

    private String mobile;

    private String password;

    private String email;

    private String picture;

    @Column(nullable = false)
    private boolean usingMfa = false;

    @Column(nullable = false)
    private String mfaKey;

    private String clientId;

    private boolean isExpired;

    private boolean isEnabled = true;

    private boolean locked;

    /**
     * 角色列表，使用 Set 确保不重复
     */
    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    private Set<Authority> authorities;

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
    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public String getMfaKey() {
        return mfaKey;
    }

    public void setMfaKey(String mfaKey) {
        this.mfaKey = mfaKey;
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