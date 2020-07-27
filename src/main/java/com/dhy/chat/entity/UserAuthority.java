package com.dhy.chat.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author vghosthunter
 */
@Entity
@Table(name = "user_authority")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class UserAuthority extends CreateAndUpdateAuditEntity implements Serializable {

    private static final long serialVersionUID = 12840110103879056L;

    @Id
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authority_id", referencedColumnName = "id")
    private Authority authority;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
