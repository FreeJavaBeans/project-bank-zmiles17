package com.revature.repository;

import com.revature.exceptions.CustomerCreationException;
import com.revature.model.Customer;
import com.revature.model.User;
import com.revature.util.ConnectionUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO implements CustomerRepository {

    ConnectionUtil util = ConnectionUtil.getSingleton();

    private final Logger logger = Logger.getLogger(CustomerDAO.class);

    @Override
    public Customer createCustomer(User user, int accountId) {
        Connection conn = util.getConnection();
        try {
            PreparedStatement prepStatement = conn.prepareStatement("insert into bank.customer (user_id, account_id) values (?, ?) returning user_id, account_id;");
            prepStatement.setInt(1, user.getUserId());
            prepStatement.setInt(2, accountId);
            ResultSet results = prepStatement.executeQuery();
            if(results.next()) {
                return new Customer(user.getUsername(), results.getInt("account_id"));
            }
        } catch(SQLException e) {
            logger.info(e.getMessage());
        }
        throw new CustomerCreationException("Customer was not created successfully");
    }

    @Override
    public Customer findCustomerByUserId(User user) {
        Connection conn = util.getConnection();
        try {
            PreparedStatement prep = conn.prepareStatement("select account_id from customer where user_id = ?;");
            prep.setInt(1, user.getUserId());
            ResultSet results = prep.executeQuery();
            if(results.next()) {
                return new Customer(user.getUserId(), user.getUsername(), results.getInt("account_id"));
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        throw new RuntimeException("Error finding customer by user id.");
    }
}
