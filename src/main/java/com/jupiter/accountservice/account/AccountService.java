package com.jupiter.accountservice.account;

import com.jupiter.accountservice.eventbus.EventBus;

public class AccountService {

    public AccountService(EventBus eventBus) {

        // Registering handler for BillingLifecycleEndedEvent
        eventBus.register(BillingLifecycleEndedEvent.class, this::handleBillingCycleEnded);
    }

    private void handleBillingCycleEnded(BillingLifecycleEndedEvent event) {
        System.out.println("Billing cycle ended for account: " + event.getAccountId());
    }

    public void onBillingCycleEnded(BillingLifecycleEndedEvent event) {
        // no-op for now: this will fail the test at runtime (Red phase)
    }

}
