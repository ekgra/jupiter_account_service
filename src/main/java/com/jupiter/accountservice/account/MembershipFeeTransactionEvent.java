package com.jupiter.accountservice.account;

import com.jupiter.accountservice.eventbus.BaseEvent;
import lombok.Getter;

@Getter
public class MembershipFeeTransactionEvent implements BaseEvent {
    private final String accountId;
    private final double amount;

    public MembershipFeeTransactionEvent(String accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

}
