package com.service.kapai.repository.model;

public enum TransactionStatus {
    FAILURE(-1), IN_PROGRESS(0), COMPLETED(1), CREATED(2);

    public final int status;

    TransactionStatus(int status) {
        this.status = status;
    }

}
