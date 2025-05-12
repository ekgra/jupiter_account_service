package com.jupiter.accountservice.account;

import com.jupiter.accountservice.eventbus.BaseEvent;
import lombok.Getter;

@Getter
public class JournalSuccessEvent implements BaseEvent {
    private final String accountId;
    private final double amount;

    public JournalSuccessEvent(String accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

}
