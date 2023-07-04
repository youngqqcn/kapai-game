package com.service.kapai.repository.model;

import org.springframework.data.relational.core.mapping.Column;

public class BuyNodeCount {
    @Column("node")
    public Integer node;

    @Column("price")
    public long price;
}
