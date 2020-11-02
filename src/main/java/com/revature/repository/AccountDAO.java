package com.revature.repository;

import com.revature.exceptions.AccountCreationException;
import com.revature.model.Account;
import com.revature.util.ConnectionUtil;

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
    public double getBalanceById(int accountId) {
        return 0;
    }
}
