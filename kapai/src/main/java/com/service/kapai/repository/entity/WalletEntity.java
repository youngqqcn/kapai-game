package com.service.kapai.repository.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 钱包
 */
@Table("wallet")
public class WalletEntity implements java.io.Serializable {

    @Id
    @Column("id")
    public Long id;

    /**
     * 钱包地址
     * 默认值: 缺省
     */
    @Column("wallet")
    public String wallet = "";

    /**
     * 直推算力
     * 默认值: 0
     */
    @Column("zt_power")
    public Long ztPower = 0L;

    /**
     * 1:禁用
     * 默认值: 0
     */
    @Column("status")
    public Integer status = 0;

    /**
     * 锁仓A代币数量
     * 默认值: 0
     */
    @Column("lock_token_a")
    public java.math.BigDecimal lockTokenA = new java.math.BigDecimal("0");

    /**
     * 节点人身份 1:小节点 2:大节点 3:超级节点
     */
    @Column("node")
    public Integer node = 0;

    /**
     * 更新时间
     * 默认值: CURRENT_TIMESTAMP
     */
    @Column("update_time")
    @LastModifiedDate
    public java.util.Date updateTime;

    /**
     * 创建时间
     * 默认值: CURRENT_TIMESTAMP
     */
    @Column("create_time")
    @CreatedDate
    public java.util.Date createTime;

}