package com.revature.model;

public class Account {
    private int accountId;
    private double balance;
    private boolean isVerified;

    public Account() {
        super();
    }

    public Account(int accountId, double balance, boolean isVerified) {
        this.accountId = accountId;
        this.balance = balance;
        this.isVerified = isVerified;
    }

    public Account(int accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.isVerified = false;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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
