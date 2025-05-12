package com.jupiter.accountservice.account;

import com.jupiter.accountservice.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import com.google.common.annotations.VisibleForTesting;

public class AccountService {

    private final EventBus eventBus;
    private final Map<String, Account> accounts = new HashMap<>();

    public AccountService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(BillingLifecycleEndedEvent.class, this::onBillingCycleEnded);
        this.eventBus.register(PaymentReceivedTransactionEvent.class, this::onPaymentReceived);
        this.eventBus.register(JournalSuccessEvent.class, this::onJournalSuccess);
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

            if (account.needsReage()) {
                eventBus.publish(new AccountReagedEvent(accountId));
                account.resetAge();
            }
        }

    }

    private void onPaymentReceived(PaymentReceivedTransactionEvent event) {
        Account account = accounts.get(event.getAccountId());
        if (account != null) {
            account.applyPayment(event.getAmount());
        }
    }

    private void onJournalSuccess(JournalSuccessEvent event) {
        Account account = accounts.get(event.getAccountId());
        if (account != null) {
            account.applyPayment(event.getAmount());

            if (account.needsReage()) {
                eventBus.publish(new AccountReagedEvent(account.getAccountId()));
                account.resetAge();
                account.resetLateFeeFlag();
            }
        }
    }

    public void applyPayment(String accountId, double amount) {
        Account account = accounts.get(accountId);
        if (account != null) {
            account.applyPayment(amount);
        }
    }

    @VisibleForTesting
    Map<String, Account> getAccounts() {
        return accounts;
    }

}
