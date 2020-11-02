package com.revature.repository;

import com.revature.exceptions.CustomerCreationException;
import com.revature.model.Customer;
import com.revature.model.User;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO implements CustomerRepository {

    ConnectionUtil util = ConnectionUtil.getSingleton();

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
            e.printStackTrace();
        }
        throw new CustomerCreationException("Customer was not created successfully");
    }
}
