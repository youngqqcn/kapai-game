package com.service.kapai.repository.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 节点购买订单
 */
@Table("buy_node_order")
public class BuyNodeOrderEntity implements java.io.Serializable {
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
     * 节点
     * 默认值: 0
     */
    @Column("node")
    public Integer node = 0;

    /**
     * 购买金额
     * 默认值: 0
     */
    @Column("price")
    public Integer price = 0;

    /**
     * 购买期数
     * 默认值: 0
     */
    @Column("period")
    public Integer period = 0;

    /**
     * 赠送EP数量
     * 默认值: 0
     */
    @Column("ep")
    public Integer ep = 0;

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
