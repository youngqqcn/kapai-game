package com.service.kapai.repository.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.service.boot.converter.enums.EnumCode;

import java.math.BigDecimal;
import java.util.HashMap;

public enum Node implements EnumCode<Integer> {
    NODE_0(0, "", 0, BigDecimal.ZERO),
    NODE_1(1, "小节点", 2000, new BigDecimal("0.1")),
    NODE_2(2, "大节点", 9000, new BigDecimal("0.2")),
    NODE_3(3, "超级节点", 40000, new BigDecimal("0.3"));

    //节点等级
    public final int node;
    public final String name;
    public final int tokenA;
    public final BigDecimal weight;

    public int price;

    public int period;
    public static final double CARD_POWER_RATIO = 0.05;
    public static final double SMALL_POWER_RATIO = 0.05;

    Node(int node, String name, int tokenA, BigDecimal weight) {
        this.node = node;
        this.name = name;
        this.tokenA = tokenA;
        this.weight = weight;
    }

    @JsonValue
    @Override
    public Integer getValue() {
        return node;
    }

    public BigDecimal getWeightPower(BigDecimal power) {
        return power.multiply(weight);
    }

    public BigDecimal getPower(BigDecimal power) {
        return power.multiply(weight).add(power);
    }

    public static Node valueOfNode(int node) {
        for (Node value : Node.values()) {
            if (value.node == node) return value;
        }
        return Node.NODE_0;
    }

    public HashMap<String, Object> getInfo(String contract) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("node", node);
        map.put("price", price);
        map.put("name", name);
        map.put("contract", contract);
        return map;
    }
}
