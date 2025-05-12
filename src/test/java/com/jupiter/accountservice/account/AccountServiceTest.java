package com.jupiter.accountservice.account;

import com.jupiter.accountservice.eventbus.EventBus;
import com.jupiter.accountservice.eventbus.BaseEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    @Test
    void shouldEmitMembershipFeeTransactionEvent_onFirstBillingCycle() {
        // Arrange
        EventBus eventBus = new EventBus();
        List<BaseEvent> capturedEvents = new ArrayList<>();
        eventBus.register(MembershipFeeTransactionEvent.class, capturedEvents::add);

        AccountService accountService = new AccountService(eventBus);

        // Act
        accountService.onBillingCycleEnded(new BillingLifecycleEndedEvent("acc-123"));

        // Assert
        boolean containsMembershipFeeEvent = capturedEvents.stream()
                .anyMatch(e -> e instanceof MembershipFeeTransactionEvent);

        assertTrue(containsMembershipFeeEvent, "Membership fee event should be emitted");
    }

    @Test
    void shouldNotEmitMembershipFeeEvent_afterFirstBillingCycle() {
        // Arrange
        EventBus eventBus = new EventBus();
        List<BaseEvent> capturedEvents = new ArrayList<>();
        eventBus.register(MembershipFeeTransactionEvent.class, capturedEvents::add);

        AccountService accountService = new AccountService(eventBus);

        // Simulate first cycle
        accountService.onBillingCycleEnded(new BillingLifecycleEndedEvent("acc-123"));
        // Simulate second cycle
        accountService.onBillingCycleEnded(new BillingLifecycleEndedEvent("acc-123"));

        // Assert: only one event emitted
        long membershipFeeEvents = capturedEvents.stream()
                .filter(e -> e instanceof MembershipFeeTransactionEvent)
                .count();

        assertEquals(1, membershipFeeEvents, "Membership fee should only be emitted once");
    }

    @Test
    void shouldEmitLateFeeTransactionEvent_whenUnpaidBalanceExists() {
        // Arrange
        EventBus eventBus = new EventBus();
        List<BaseEvent> capturedEvents = new ArrayList<>();
        eventBus.register(LateFeeTransactionEvent.class, capturedEvents::add);

        AccountService accountService = new AccountService(eventBus);

        // Simulate account creation and billing cycle (to post membership fee)
        accountService.onBillingCycleEnded(new BillingLifecycleEndedEvent("acc-456"));

        // Simulate another cycle (balance is unpaid)
        accountService.onBillingCycleEnded(new BillingLifecycleEndedEvent("acc-456"));

        // Assert: late fee event was emitted
        boolean containsLateFee = capturedEvents.stream()
                .anyMatch(e -> e instanceof LateFeeTransactionEvent);

        assertTrue(containsLateFee, "Late fee event should be emitted when balance is unpaid");
    }

    @Test
    void shouldEmitAccountReagedEvent_whenBalanceIsCleared() {
        // Arrange
        EventBus eventBus = new EventBus();
        List<BaseEvent> capturedEvents = new ArrayList<>();
        eventBus.register(AccountReagedEvent.class, capturedEvents::add);

        AccountService accountService = new AccountService(eventBus);

        // 1st cycle: charge membership fee
        accountService.onBillingCycleEnded(new BillingLifecycleEndedEvent("acc-789"));
        // 2nd cycle: no payment, age increases
        accountService.onBillingCycleEnded(new BillingLifecycleEndedEvent("acc-789"));

        // Simulate full payment (clears 799 + 35)
        accountService.applyPayment("acc-789", 834.00); // We'll add this method

        // 3rd cycle: should now emit AccountReagedEvent
        accountService.onBillingCycleEnded(new BillingLifecycleEndedEvent("acc-789"));

        // Assert
        boolean reaged = capturedEvents.stream().anyMatch(e -> e instanceof AccountReagedEvent);
        assertTrue(reaged, "Account should re-age when balance is cleared");
    }

    @Test
    void shouldReduceBalance_whenPaymentEventIsReceived() {
        // Arrange
        EventBus eventBus = new EventBus();
        AccountService accountService = new AccountService(eventBus);

        // Trigger initial membership fee
        accountService.onBillingCycleEnded(new BillingLifecycleEndedEvent("acc-777"));

        // Act: simulate payment via event
        eventBus.publish(new PaymentReceivedTransactionEvent("acc-777", 799.00));

        // Assert
        Account account = accountService.getAccounts().get("acc-777");
        assertNotNull(account);
        assertEquals(0.0, account.getBalance(), 0.001, "Balance should be cleared after payment");
    }

    @Test
    void shouldApplyJournaledAmount_whenJournalSuccessEventReceived() {
        // Arrange
        EventBus eventBus = new EventBus();
        AccountService accountService = new AccountService(eventBus);

        // 1. Simulate billing cycle to create fee
        accountService.onBillingCycleEnded(new BillingLifecycleEndedEvent("acc-555"));

        // 2. Simulate journal success for payment
        eventBus.publish(new JournalSuccessEvent("acc-555", 799.00));

        // Assert balance is now 0
        Account account = accountService.getAccounts().get("acc-555");
        assertNotNull(account);
        assertEquals(0.0, account.getBalance(), 0.001, "Balance should be cleared after journal success");
    }

}
