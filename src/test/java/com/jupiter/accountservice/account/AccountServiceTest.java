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

}
