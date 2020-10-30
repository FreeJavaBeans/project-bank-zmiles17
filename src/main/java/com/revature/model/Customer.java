package com.revature.model;

import java.lang.System;

public class Customer extends User {

    private int customerId;
    private int accountId;

    public Customer(String username, String password) {
        super(username, password);
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    private void applyForBankAccount(int customerId, double balance) {
        if(this.accountId > 0) {
            System.out.println("You already have a bank account. We only allow one bank account per customer.");
        } else {
            this.accountId = Employee.applyForAccount(customerId, balance);
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
