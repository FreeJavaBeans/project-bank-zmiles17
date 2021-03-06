package com.revature.model;

import java.lang.System;

public class Customer extends User {

    private int accountId;

    public Customer() {
        super();
    }

    public Customer(String username, int accountId) {
        super(username);
        this.accountId = accountId;
    }

    public Customer(int userId, String username, int accountId) {
        super(userId, username);
        this.accountId = accountId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    private void applyForBankAccount(double balance) {
        if(this.accountId > 0) {
            System.out.println("You already have a bank account. We only allow one bank account per customer.");
        } else {
            this.accountId = Employee.applyForAccount(balance);
        }
    }

    private String viewBalance() {
        StringBuilder sb = new StringBuilder();
        if(accountId > 0) {
            sb.append("$");
//          need to query database for account balance
        } else {
            sb.append("You do not have an account. Please apply for a bank account to view your balance.");
        }
        return sb.toString();
    }

}
