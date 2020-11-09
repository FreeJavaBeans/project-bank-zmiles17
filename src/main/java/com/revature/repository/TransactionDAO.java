package com.revature.repository;

import com.revature.model.Transaction;

import java.util.List;

public class TransactionDAO implements TransactionRepository {
    @Override
    public Transaction createTransaction(int fromAccountId, int toAccountId, double amount) {
        return null;
    }

    @Override
    public void updateTransactionStatus(int transactionId) {

    }

    @Override
    public List<Transaction> getTransactionsToAccountId(int toAccountId) {
        return null;
    }
}
