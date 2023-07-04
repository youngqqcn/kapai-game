package com.service.kapai.repository.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 等级模型
 */
@Table("level_model")
public class LevelModelEntity implements java.io.Serializable {

    @Id
    @Column("id")
    public Long id;

    /**
     * 名称
     * 默认值: 缺省
     */
    @Column("name")
    public String name = "";

    /**
     * 此等级需要持有的卡牌ID
     * 默认值: 缺省
     */
    @Column("card_model_id")
    public Long cardModelId = 0L;

    /**
     * 此等级需要的小区业绩
     * 默认值: 0.000000000000000000
     */
    @Column("small_power")
    public Long smallPower = 0L;

    /**
     * 加权
     * 默认值: 0.00
     */
    @Column("weight")
    public java.math.BigDecimal weight = new java.math.BigDecimal("0");

    /**
     * 日产出上限
     * 默认值: 0
     */
    @Column("upper_limit")
    public Integer upperLimit = 0;

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