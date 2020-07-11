package com.dhy.chat.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Authority implements GrantedAuthority {

    private static final long serialVersionUID = 5496002851233236900L;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private int id;

    private int uid;

    private String authority;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority1 = (Authority) o;
        return id == authority1.id &&
                uid == authority1.uid &&
                Objects.equals(authority, authority1.authority);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, uid, authority);
    }
}