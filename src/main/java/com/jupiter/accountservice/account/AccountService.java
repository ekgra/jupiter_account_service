package com.jupiter.accountservice.account;

import com.jupiter.accountservice.eventbus.EventBus;

public class AccountService {

    private final EventBus eventBus;

    public AccountService(EventBus eventBus) {
        this.eventBus = eventBus;

        // Registering handler for BillingLifecycleEndedEvent
        this.eventBus.register(BillingLifecycleEndedEvent.class, this::handleBillingCycleEnded);
    }

    private void handleBillingCycleEnded(BillingLifecycleEndedEvent event) {
        System.out.println("Billing cycle ended for account: " + event.getAccountId());
    }
}
