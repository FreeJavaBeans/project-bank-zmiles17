package com.revature.repository;

import com.revature.model.Account;

public interface AccountRepository {

    public Account createAccount(double balance);

    public Account getAccountById(int accountId);

    public Account updateBalance(double amount, int accountId);
}
