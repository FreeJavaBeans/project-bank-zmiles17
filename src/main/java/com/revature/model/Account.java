package com.revature.model;

public class Account {
    private int accountId;
    private int customerId;
    private double balance;
    private boolean isVerified;

    public Account(int customerId, double balance) {
        this.customerId = customerId;
        this.balance = balance;
        this.isVerified = false;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
