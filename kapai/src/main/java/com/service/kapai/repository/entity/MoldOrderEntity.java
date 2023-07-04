package com.service.kapai.repository.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 铸造订单
 */
@Table("mold_order")
public class MoldOrderEntity implements java.io.Serializable {

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
     * 卡牌模型ID
     * 默认值: 缺省
     */
    @Column("card_model_id")
    public Long cardModelId = 0L;

    /**
     * type 0: 不使用EP  1: 使用EP
     * 默认值: 0
     */
    @Column("type")
    public Integer type = 0;


    /**
     * A代币数量
     * 默认值: 0.000000000000000000
     */
    @Column("price")
    public java.math.BigDecimal price = new java.math.BigDecimal("0");


    /**
     * A代币数量
     * 默认值: 0.000000000000000000
     */
    @Column("token_a")
    public java.math.BigDecimal tokenA = new java.math.BigDecimal("0");

    /**
     * EP数量
     * 默认值: 0.000000000000000000
     */
    @Column("ep")
    public java.math.BigDecimal ep = new java.math.BigDecimal("0");

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
    public Integer status = 2;

    /**
     * 卡牌数据
     * 默认值: 缺省
     */
    @Column("card_data")
    public String cardData;

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