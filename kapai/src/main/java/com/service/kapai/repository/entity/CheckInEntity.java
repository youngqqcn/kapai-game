package com.service.kapai.repository.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.service.boot.converter.enums.EnumCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

/**
 * 资产变更记录
 */
@Table("check_in")
public class CheckInEntity implements java.io.Serializable {

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
     * 资产类型 1:A代币 2:B代币
     * 默认值: 0
     */
    @Column("type")
    public Type type = Type.UNKNOWN;

    /**
     * 数量
     * 默认值: 0
     */
    @Column("amount")
    public java.math.BigDecimal amount = new java.math.BigDecimal("0");

    @Column("status")
    public Status status = Status.IN_PROGRESS;

    /**
     * 创建时间
     * 默认值: CURRENT_TIMESTAMP
     */
    @Column("date")
    public LocalDate date;

    public enum Type implements EnumCode<Integer> {

        UNKNOWN(0, "未知"), TOKEN_A(1, "A代币"), TOKEN_B(2, "B代币");

        public final int type;
        public final String name;

        Type(int type, String name) {
            this.type = type;
            this.name = name;
        }

        @JsonValue
        @Override
        public Integer getValue() {
            return type;
        }

        public static Type valueOf(int type) {
            Type[] values = Type.values();
            for (Type t : values) {
                if (t.type == type) {
                    return t;
                }
            }
            return Type.UNKNOWN;
        }
    }

    public enum Status implements EnumCode<Integer> {
        IN_PROGRESS(0, "未签到"), COMPLETED(1, "已签到");

        public final int status;
        public final String name;

        Status(int status, String name) {
            this.status = status;
            this.name = name;
        }

        @Override
        public Integer getValue() {
            return status;
        }
    }

}