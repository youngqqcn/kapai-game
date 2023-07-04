package com.service.kapai.repository.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.service.boot.converter.enums.EnumCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * 钱包持有的卡牌
 */
@Table("wallet_card")
public class WalletCardEntity implements java.io.Serializable {

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
     * 算力
     * 默认值: 0
     */
    @Column("power")
    public Long power = 0L;

    /**
     * 产出
     * 默认值: 0.000000000000000000
     */
    @Column("output")
    public java.math.BigDecimal output = new java.math.BigDecimal("0");

    /**
     * 持有天数
     * 默认值: 0
     */
    public Integer days = 0;

    /**
     * 0:正在释放 1:释放完毕
     * 默认值: 0
     */
    @Column("status")
    public Status status;

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

    public enum Status implements EnumCode<Integer> {
        RELEASING(0), RELEASED(1);

        public final int status;

        Status(int status) {
            this.status = status;
        }

        @JsonValue
        @Override
        public Integer getValue() {
            return status;
        }
    }

    public static BigDecimal getPowerB(WalletCardEntity entity) {
        return new BigDecimal(entity.power).multiply(getBQ(entity.days));
    }

    public static BigDecimal getBQ(Integer days) {
        if (days == null) return BigDecimal.ZERO;
        if (days >= 100) {
            return new BigDecimal("1");
        }
        if (days >= 80) {
            return new BigDecimal("0.8");
        }
        if (days >= 60) {
            return new BigDecimal("0.6");
        }
        if (days >= 40) {
            return new BigDecimal("0.4");
        }
        if (days >= 20) {
            return new BigDecimal("0.2");
        }
        return BigDecimal.ZERO;
    }
}