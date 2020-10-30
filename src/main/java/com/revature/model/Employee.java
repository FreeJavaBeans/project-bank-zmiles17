package com.revature.model;

import java.util.PriorityQueue;
import java.util.Queue;
import java.lang.System;

public class Employee extends User {

    private static Queue<Account> accountsToVerify = new PriorityQueue<>();

    public Employee(String username, String password) {
        super(username, password);
    }

    public static int applyForAccount(int customerId, double balance) {
        Account acc = new Account(customerId, balance);
        accountsToVerify.add(acc);
        System.out.println("You have successfully applied for a bank account. You will be able to access your account once an employee verifies it");
        return acc.getAccountId();
    }

    public static void verifyBankAccount() {
        Account toVerify = accountsToVerify.remove();
        double balance = toVerify.getBalance();
        if(balance > 100 && balance < 100000) {
            toVerify.setVerified(true);
            System.out.println("Bank account approved");
        } else {
            System.out.println("Bank account was not approved");
        }
    }
}
