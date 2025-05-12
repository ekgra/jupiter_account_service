package com.jupiter.accountservice.account;

import lombok.Getter;

public class Account {
    @Getter
    private final String accountId;
    private boolean membershipFeePosted = false;

    public Account(String accountId) {
        this.accountId = accountId;
    }

    public boolean needsMembershipFee() {
        return !membershipFeePosted;
    }

    public void markMembershipFeePosted() {
        this.membershipFeePosted = true;
    }
}
