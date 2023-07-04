package com.service.kapai.repository.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * 资产变更记录
 */
@Table("swap_ep_order")
public class SwapEPOrderEntity implements java.io.Serializable {
    @Id
    @Column("id")
    public Long id;
    /**
     * 钱包ID
     * 默认值: 缺省
     */
    @Column("wallet_id")
    public Long walletId = 0L;

    /**
     * 订单ID
     * 默认值: 0
     */
    @Column("order_id")
    public String orderId = "";

    /**
     * 金额
     * 默认值: 0
     */
    @Column("amount")
    public BigDecimal amount = BigDecimal.ZERO;

    /**
     * 交易hash
     * 默认值: 缺省
     */
    @Column("tx_hash")
    public String txHash = "";

    /**
     * 状态 -1失败 0未完成 1已完成
     * 默认值: 0
     */
    @Column("status")
    public Integer status = 0;

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
