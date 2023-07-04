package com.service.kapai.repository.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 资产变更记录
 */
@Table("wallet_power")
public class WalletPowerEntity implements java.io.Serializable {
    /**
     * 钱包ID
     * 默认值: 缺省
     */
    @Id
    @Column("wallet_id")
    public Long walletId = 0L;

    /**
     * 节点
     * 默认值: 0
     */
    @Column("small_power")
    public Long smallPower = 0L;

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
