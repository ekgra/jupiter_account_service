package com.jupiter.accountservice;

import com.jupiter.accountservice.account.AccountService;
import com.jupiter.accountservice.account.BillingLifecycleEndedEvent;
import com.jupiter.accountservice.eventbus.EventBus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            EventBus eventBus = new EventBus();
            AccountService accountService = new AccountService(eventBus);

            eventBus.publish(new BillingLifecycleEndedEvent("acc-101"));
        };
    }
}
