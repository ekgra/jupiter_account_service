package com.jupiter.accountservice.account;

import com.jupiter.accountservice.eventbus.BaseEvent;
import lombok.Getter;

@Getter
public class PaymentReceivedTransactionEvent implements BaseEvent {
    private final String accountId;
    private final double amount;

    public PaymentReceivedTransactionEvent(String accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

}
