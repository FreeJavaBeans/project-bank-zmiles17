package com.revature.repository;

import com.revature.exceptions.AccountCreationException;
import com.revature.model.Account;
import com.revature.util.ConnectionUtil;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements AccountRepository {

    private ConnectionUtil connectionUtil = ConnectionUtil.getSingleton();

    private final Logger logger = Logger.getLogger(AccountDAO.class);

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
            logger.info(e.getMessage());
        }
        throw new AccountCreationException("Your account was not created successfully. " +
                "Balance must be greater than or equal to $100.00 and less than or equal to 10 million.");
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
            logger.info(e.getMessage());
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
                BigDecimal bd = BigDecimal.valueOf(results.getDouble("balance")).setScale(2, RoundingMode.HALF_UP);
                return new Account(accountId, bd.doubleValue() , results.getBoolean("is_verified"));
            }
        } catch(SQLException e) {
            logger.info(e.getMessage());
        }
        throw new RuntimeException("Error updating balance. Minimum or maximum balance breached.");
    }

    @Override
    public List<Account> getAllAccounts() {
        Connection conn = connectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            ResultSet resultSet = conn.createStatement().executeQuery("select * from account;");
            while(resultSet.next()) {
                accounts.add(new Account(resultSet.getInt("account_id"),
                        resultSet.getDouble("balance"),
                        resultSet.getBoolean("is_verified")));
            }
            return accounts;
        } catch(SQLException e) {
            logger.info(e.getMessage());
        }
        throw new RuntimeException("Error retrieving all accounts.");
    }

    @Override
    public void updateVerification(int accId, boolean verified) {
        Connection conn = connectionUtil.getConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("update account set is_verified = ? where account_id = ?;");
            preparedStatement.setBoolean(1, verified);
            preparedStatement.setInt(2, accId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }

    @Override
    public Account getAccountByCustomerUsername(String username) {
        Connection conn = connectionUtil.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select * from account where account_id = " +
                    "(select account_id from customer where user_id = " +
                    "(select user_id from \"user\" where \"user\".username = ?));");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return new Account(resultSet.getInt("account_id"),
                        resultSet.getDouble("balance"),
                        resultSet.getBoolean("is_verified"));
            }
        }catch (SQLException e) {
            logger.info(e.getMessage());
        }
        throw new RuntimeException("Error retrieving account by customer username.");
    }
}
