package com.jupiter.accountservice.account;

import com.jupiter.accountservice.eventbus.BaseEvent;
import lombok.Getter;

@Getter
public class BillingLifecycleEndedEvent implements BaseEvent {
    private final String accountId;

    public BillingLifecycleEndedEvent(String accountId) {
        this.accountId = accountId;
    }

}
