package com.service.kapai.repository.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("token_destroy")
public class TokenDestroyEntity {
    @Id
    @Column("id")
    public Long id;

    @Column("token")
    public String token;

    @Column("day")
    public LocalDate day;

    @Column("last_amount")
    public BigDecimal lastAmount = BigDecimal.ZERO;

    @Column("current_amount")
    public BigDecimal currentAmount = BigDecimal.ZERO;

    @Column("create_time")
    @CreatedDate
    public java.util.Date createTime;
}
