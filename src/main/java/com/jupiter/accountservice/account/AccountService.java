package com.jupiter.accountservice.account;

import com.jupiter.accountservice.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class AccountService {

    private final EventBus eventBus;
    private final Map<String, Account> accounts = new HashMap<>();

    public AccountService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(BillingLifecycleEndedEvent.class, this::onBillingCycleEnded);
    }

    public void onBillingCycleEnded(BillingLifecycleEndedEvent event) {
        String accountId = event.getAccountId();
        Account account = accounts.computeIfAbsent(accountId, Account::new);

        if (account.needsMembershipFee()) {
            eventBus.publish(new MembershipFeeTransactionEvent(accountId, 799.00));
            account.markMembershipFeePosted();
        } else {
            account.ageIfUnpaid();

            if (account.needsLateFee()) {
                eventBus.publish(new LateFeeTransactionEvent(accountId, 35.00));
                account.markLateFeePosted();
            }
        }
    }

    // Optional: expose state for testing/debugging
    Map<String, Account> getAccounts() {
        return accounts;
    }
}
