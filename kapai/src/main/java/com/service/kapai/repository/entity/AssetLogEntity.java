package com.service.kapai.repository.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.service.boot.converter.enums.EnumCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 资产变更记录
 */
@Table("asset_log")
public class AssetLogEntity implements java.io.Serializable {

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
     * 资产类型 1:算力 2:A代币 3:B代币 4:业绩, 5:EP
     * 默认值: 0
     */
    @Column("type")
    public Type type = Type.UNKNOWN;

    /**
     * 关联ID
     * 默认值: 0
     */
    @Column("card_model_id")
    public Long cardModelId = 0L;

    /**
     * 变更前数量
     * 默认值: 0.000000000000000000
     */
    @Column("before_amount")
    public java.math.BigDecimal beforeAmount = new java.math.BigDecimal("0");

    /**
     * 变更后数量
     * 默认值: 0.000000000000000000
     */
    @Column("after_amount")
    public java.math.BigDecimal afterAmount = new java.math.BigDecimal("0");

    /**
     * 变更数量
     * 默认值: 0.000000000000000000
     */
    @Column("amount")
    public java.math.BigDecimal amount = new java.math.BigDecimal("0");

    /**
     * 来源
     * [{"name":"算力","type":1,"source":{"1":"铸造","2":"下级铸造获得奖励"}},{"name":"A代币","type":2,"source":{"1":"","2":""}},{"name":"B代币","type":3,"source":{"1":"","2":""}},{"name":"业绩","type":4,"source":{"1":"","2":""}},{"name":"EP","type":5,"source":{"1":"铸造","2":"铸造失败回退"}}]
     * 默认值: 0
     */
    @Column("source")
    public Integer source = 0;

    /**
     * 附带信息
     * 默认值: 缺省
     */
    @Column("extra")
    public String extra;

    /**
     * 创建时间
     * 默认值: CURRENT_TIMESTAMP
     */
    @Column("create_time")
    @CreatedDate
    public java.util.Date createTime;

    public enum Type implements EnumCode<Integer> {

        UNKNOWN(0, "未知"), Power(1, "算力"), TOKEN_A(2, "A代币"), TOKEN_B(3, "B代币"), EP(5, "EP"), USDT(6, "USD");

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

    public enum SourcePower implements EnumCode<Integer> {
        UNKNOWN(0, "未知"), MOLD(1, "铸造"), ZT_MOLD(2, "直推");

        public final int source;
        public final String name;

        SourcePower(int source, String name) {
            this.source = source;
            this.name = name;
        }

        @Override
        public Integer getValue() {
            return source;
        }
    }

    public enum SourceEP implements EnumCode<Integer> {
        UNKNOWN(0, "未知"), SWAP(1, "兑换"), MOLD(2, "铸造"), MOLD_RETURN(3, "铸造失败返还"), BUY_NODE(4, "购买节点赠送");

        public final int source;
        public final String name;

        SourceEP(int source, String name) {
            this.source = source;
            this.name = name;
        }

        @Override
        public Integer getValue() {
            return source;
        }
    }

    public enum SourceToken implements EnumCode<Integer> {
        UNKNOWN(0, "未知"),
        WITHDRAW(1, "提现"),
        CHECK_IN(2, "签到"),
        STATIC_RELEASE(3, "静态释放"),
        DYNAMIC_RELEASE(4, "动态释放"),
        BUY_NODE(5, "购买节点"),
        NODE_MOLD(6, "直推铸造释放TokenA"),
        TEAM_NODE_LEVEL(7, "节点分红TokenA"),
        USDT_NODE_DYNAMIC(8, "USD节点动态收益"),
        NODE_RELEASE(9, "节点分红"),
        ;

        public final int source;
        public final String name;

        SourceToken(int source, String name) {
            this.source = source;
            this.name = name;
        }

        @Override
        public Integer getValue() {
            return source;
        }
    }
}