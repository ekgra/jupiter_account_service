package com.jupiter.accountservice.account;

import com.jupiter.accountservice.eventbus.BaseEvent;
import lombok.Getter;

@Getter
public class AccountReagedEvent implements BaseEvent {
    private final String accountId;

    public AccountReagedEvent(String accountId) {
        this.accountId = accountId;
    }

}
