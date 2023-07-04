package com.service.kapai.repository.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 推荐关系
 */
@Table("relation")
public class RelationEntity implements java.io.Serializable {

    @Id
    @Column("wallet_id")
    public Long walletId;

    /**
     * 上级
     * 默认值: 0
     */
    @Column("superior_id")
    public Long superiorId = 0L;

    /**
     * 关系链
     * 默认值: 缺省
     */
    @Column("relation_path")
    public String relationPath = "";

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