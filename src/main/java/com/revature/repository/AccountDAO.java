package com.revature.repository;

import com.revature.exceptions.AccountCreationException;
import com.revature.model.Account;
import com.revature.util.ConnectionUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO implements AccountRepository {

    private ConnectionUtil connectionUtil = ConnectionUtil.getSingleton();

    @Override
    public Account createAccount(double balance) {
        Connection connection = connectionUtil.getConnection();
        try {
            PreparedStatement prepStatement = connection.prepareStatement("insert into bank.account (balance) values (?) returning account_id, balance, is_verified;");
            prepStatement.setDouble(1, balance);
            ResultSet results = prepStatement.executeQuery();
            if(results.next()) {
                return new Account(results.getInt("account_id"), results.getDouble("balance"), results.getBoolean("is_verified"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        throw new AccountCreationException("Your account was not created successfully. Please review the balance amount you have provided.");
    }

    @Override
    public Account getAccountById(int accountId) {
        Connection conn = connectionUtil.getConnection();
        try {
            PreparedStatement prepStatement = conn.prepareStatement("select * from bank.account where account_id = ?;");
            prepStatement.setInt(1, accountId);
            ResultSet results = prepStatement.executeQuery();
            if(results.next()) {
                return new Account(results.getInt("account_id"), results.getDouble("balance"), results.getBoolean("is_verified"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Error occurred while retrieving your account.");
    }

    @Override
    public Account updateBalance(double amount, int accountId) {
        Connection conn = connectionUtil.getConnection();
        try {
            PreparedStatement prep = conn.prepareStatement("update account set balance = ? where account_id = ? returning balance, is_verified;");
            prep.setDouble(1, amount);
            prep.setInt(2, accountId);
            ResultSet results = prep.executeQuery();
            if(results.next()) {
                BigDecimal bd = new BigDecimal(results.getDouble("balance")).setScale(2, RoundingMode.HALF_UP);
                return new Account(accountId, bd.doubleValue() , results.getBoolean("is_verified"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Error updating balance. Minimum or maximum balance breached.");
    }
}
