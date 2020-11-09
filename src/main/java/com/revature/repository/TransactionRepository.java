package com.revature.repository;

import com.revature.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    Transaction createTransaction(int fromAccountId, int toAccountId, double amount);

    void updateTransactionStatus(int transactionId);

    List<Transaction> getTransactionsToAccountId(int toAccountId);
}
