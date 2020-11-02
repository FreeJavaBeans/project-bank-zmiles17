package com.revature.repository;

import com.revature.exceptions.UserNotCreatedException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.model.User;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements UserRepository {

    private ConnectionUtil connectionUtil = ConnectionUtil.getSingleton();

    @Override
    public User createUser(String username, String password) {
        Connection connection = connectionUtil.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into Bank.\"user\" (username, password) values (?, ?) returning username");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet results = preparedStatement.executeQuery();
            if(results.next()) {
                return new User(results.getString("username"));
            }
        } catch(SQLException e) {
            System.out.println("Error occurred while trying to create a new user");
            e.printStackTrace();
        }
        throw new UserNotCreatedException("User was not created successfully");
    }

    @Override
    public User findUserByUsername(String username) {
        Connection connection = connectionUtil.getConnection();
        try {
            PreparedStatement prepStatement = connection.prepareStatement("select username from User where username = ?");
            prepStatement.setString(1, username);
            ResultSet results = prepStatement.executeQuery();
            return new User(results.getString("username"));
        } catch (SQLException e) {
            System.out.println("Error occurred while trying find a user in the database");
            e.printStackTrace();
        }
        throw new UserNotFoundException("User was not found");
    }
}