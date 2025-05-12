package com.jupiter.accountservice.account;

import com.jupiter.accountservice.eventbus.EventBus;

import java.util.HashSet;
import java.util.Set;

public class AccountService {

    private final EventBus eventBus;
    private final Set<String> billedAccounts = new HashSet<>(); // ✅ Track accounts already billed

    public AccountService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(BillingLifecycleEndedEvent.class, this::onBillingCycleEnded);
    }

    public void onBillingCycleEnded(BillingLifecycleEndedEvent event) {
        String accountId = event.getAccountId();

        // ✅ Only emit the fee once per account
        if (!billedAccounts.contains(accountId)) {
            eventBus.publish(new MembershipFeeTransactionEvent(accountId, 799.00));
            billedAccounts.add(accountId); // ✅ mark as billed
        }
    }
}
