package com.revature.model;

public class Customer extends User {

    private Account bankAccount;

    public Customer(String username, String password) {
        super(username, password);
    }

    private void applyForBankAccount(double balance) {
        if(this.bankAccount != null) {
            this.bankAccount = Employee.verifyAccount(balance);
        } else {
            System.out.println("You already have a bank account. We only allow one bank account per customer.");
        }
    }

    private String viewBalance() {
        StringBuilder sb = new StringBuilder();
        if(bankAccount != null) {
            sb.append("$");
            sb.append(bankAccount.getBalance());
        } else {
            sb.append("You do not have an account. Please apply for a bank account to view your balance.");
        }
        return sb.toString();
    }

}
