package com.dhy.chat.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_authority")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class UserAuthority extends CreateAndUpdateAuditEntity implements Serializable {

    private static final long serialVersionUID = 12840110103879056L;

    @Id
    private String userId;

    @Id
    private String authorityId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authorityId", referencedColumnName = "id")
    private Authority authority;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
}
