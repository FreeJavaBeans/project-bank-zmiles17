package com.revature.model;

public class Employee extends User {

    public Employee(String username, String password) {
        super(username, password);
    }

    public static Account verifyAccount(double balance) {
        Account account = null;
        if(balance > 100 && balance < 100000) {
            account = new Account(balance);
            System.out.println("Your account has been approved!");
        }
        return account;
    }
}
