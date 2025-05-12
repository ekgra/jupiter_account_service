package com.jupiter.accountservice.account;

import lombok.Getter;

@Getter
public class Account {
    private final String accountId;
    private boolean membershipFeePosted = false;
    private boolean lateFeePosted = false;
    private double balance = 0.0;
    private int ageInDays = 0;


    public Account(String accountId) {
        this.accountId = accountId;
    }

    public boolean needsMembershipFee() {
        return !membershipFeePosted;
    }

    public void markMembershipFeePosted() {
        this.membershipFeePosted = true;
        this.balance += 799.00; // hardcoded for now
    }

    public boolean hasUnpaidBalance() {
        return balance > 0.0;
    }

    public void ageIfUnpaid() {
        if (hasUnpaidBalance()) {
            this.ageInDays += 30;
        }
    }

    public boolean needsLateFee() {
        return hasUnpaidBalance() && ageInDays >= 30 && !lateFeePosted;
    }

    public void markLateFeePosted() {
        this.lateFeePosted = true;
        this.balance += 35.00; // hardcoded for now
    }

    public void applyPayment(double amount) {
        this.balance -= amount;
        if (this.balance <= 0) {
            this.balance = 0;
        }
    }

    public boolean needsReage() {
        return balance == 0 && ageInDays >= 30;
    }

    public void resetAge() {
        this.ageInDays = 0;
    }

}
