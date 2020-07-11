package com.dhy.chat.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@EntityListeners(AuditingEntityListener.class)
public class Test {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
}
