package com.dhy.chat.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "authority", indexes = {
    @Index(name = "idx_authority",columnList = "authority", unique = true)
})
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Authority extends CreateAndUpdateAuditEntity implements GrantedAuthority {

    private static final long serialVersionUID = 5496002851233236900L;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id")
    private String id;

    private String authority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}