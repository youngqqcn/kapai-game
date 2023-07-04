package com.service.kapai.repository.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.service.boot.converter.enums.EnumCode;

public enum TransactionStatus implements EnumCode<Integer> {
    FAILURE(-1), IN_PROGRESS(0), COMPLETED(1), CREATED(2);

    public final int status;

    TransactionStatus(int status) {
        this.status = status;
    }

    @JsonValue
    @Override
    public Integer getValue() {
        return status;
    }
}
