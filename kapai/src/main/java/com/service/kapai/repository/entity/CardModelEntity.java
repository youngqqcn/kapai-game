package com.service.kapai.repository.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 卡牌模型
 */
@Table("card_model")
public class CardModelEntity implements java.io.Serializable {

    @Id
    @Column("id")
    public Long id;

    /**
     * 卡牌名称
     * 默认值: 缺省
     */
    @Column("name")
    public String name = "";

    /**
     * 单价
     * 默认值: 0
     */
    @Column("unit_price")
    public Integer unitPrice = 0;

    /**
     * 铸造卡牌赠送的算力
     * 默认值: 0
     */
    @Column("power")
    public Integer power = 0;

    /**
     * 铸造卡牌产出
     * 默认值: 0
     */
    @Column("output")
    public Integer output = 0;

    /**
     * 铸造需要的EP数量
     * 默认值: 0
     */
    @Column("ep")
    public Integer ep = 0;

    /**
     * 直推赠送的算力
     * 默认值: 0
     */
    @Column("zt_power")
    public Integer ztPower = 0;

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