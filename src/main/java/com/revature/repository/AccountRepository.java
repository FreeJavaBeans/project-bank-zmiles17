package com.revature.repository;

import com.revature.model.Account;

import java.util.List;

public interface AccountRepository {

    public Account createAccount(double balance);

    public Account getAccountById(int accountId);

    public Account updateBalance(double amount, int accountId);

    public List<Account> getAllAccounts();

    public void updateVerification(int accId, boolean verified);

    public Account getAccountByCustomerUsername(String username);
}
