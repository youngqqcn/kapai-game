package com.service.kapai.repository.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.service.kapai.repository.entity.WalletCardEntity;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

public class WalletCardInfo {

    @Column("id")
    public Long id;

    @Column("name")
    public String name = "";

    @Column("unit_price")
    public Integer unitPrice = 0;

    @Column("power")
    public Integer power = 0;

    @Column("output")
    public Integer output = 0;

    @Column("ep")
    public Integer ep = 0;

    @Column("days")
    public Integer days = 0;

    @Column("wallet_id")
    public Long walletId;

    @Column("my_power")
    public Long myPower = 0L;

    @Column("my_output")
    public java.math.BigDecimal myOutput = new java.math.BigDecimal("0");

    @Column("quantity")
    public Integer quantity = 0;

    /**
     * 0 待铸造
     * 1 生产中
     * -1 已过期
     */
    public int status = 0;

    public BigDecimal myPowerB = BigDecimal.ZERO;

//    @JsonGetter
//    public BigDecimal getMyPowerB() {
//        return new BigDecimal(myPower).multiply(WalletCardEntity.getBQ(days));
//    }
}
