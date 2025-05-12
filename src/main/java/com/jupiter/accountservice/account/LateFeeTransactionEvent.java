package com.jupiter.accountservice.account;

import com.jupiter.accountservice.eventbus.BaseEvent;

public class LateFeeTransactionEvent implements BaseEvent {
    private final String accountId;
    private final double amount;

    public LateFeeTransactionEvent(String accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }
}
