package com.revature.model;

public class Account {

    private Customer customer;
    private double balance;
    private boolean isVerified;

    public Account(Customer customer, double balance) {
        this.customer = customer;
        this.balance = balance;
        this.isVerified = false;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
