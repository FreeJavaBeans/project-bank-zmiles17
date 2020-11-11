package com.revature.repository;

import com.revature.model.Transaction;
import com.revature.util.ConnectionUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO implements TransactionRepository {

    ConnectionUtil singleton = ConnectionUtil.getSingleton();

    private final Logger logger = Logger.getLogger(TransactionDAO.class);

    @Override
    public void createTransaction(int fromAccountId, int toAccountId, double amount) {
        Connection conn = singleton.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("insert into transaction (from_account, to_account, amount) values (?, ?, ?);");
            statement.setInt(1, fromAccountId);
            statement.setInt(2, toAccountId);
            statement.setDouble(3, amount);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateTransactionStatus(int transactionId) {
        Connection conn = singleton.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("update transaction set pending = false where transaction_id = ?;");
            statement.setInt(1, transactionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> getTransactionsToAccountId(int toAccountId) {
        Connection conn = singleton.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select * from transaction where to_account = ?;");
            statement.setInt(1, toAccountId);
            ResultSet resultSet = statement.executeQuery();
            List<Transaction> transactions = new ArrayList<>();
            while(resultSet.next()) {
                if(resultSet.getBoolean("pending")) {
                    transactions.add(new Transaction(resultSet.getInt("transaction_id"),
                            resultSet.getInt("from_account"),
                            resultSet.getInt("to_account"),
                            resultSet.getDouble("amount"),
                            resultSet.getBoolean("pending")));
                }
            }
            return transactions;
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        throw new RuntimeException("Error while retrieving transactions to account id: " + toAccountId);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        Connection conn = singleton.getConnection();
        try {
            List<Transaction> transactions = new ArrayList<>();
            ResultSet resultSet = conn.createStatement().executeQuery("select * from transaction;");
            while(resultSet.next()) {
                transactions.add(new Transaction(resultSet.getInt("transaction_id"),
                        resultSet.getInt("from_account"),
                        resultSet.getInt("to_account"),
                        resultSet.getDouble("amount"),
                        resultSet.getBoolean("pending")));
            }
            return transactions;
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        throw new RuntimeException("Error while retrieving all transactions");
    }
}
