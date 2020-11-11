package com.revature.repository;

import com.revature.exceptions.UserNotCreatedException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.model.User;
import com.revature.util.ConnectionUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements UserRepository {

    private final Logger logger = Logger.getLogger(UserDAO.class);

    private ConnectionUtil connectionUtil = ConnectionUtil.getSingleton();

    @Override
    public User createUser(String username, String password) {
        Connection connection = connectionUtil.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into bank.user (username, password) values (?, ?) returning user_id, username");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet results = preparedStatement.executeQuery();
            if(results.next()) {
                return new User(results.getInt("user_id"), results.getString("username"));
            }
        } catch(SQLException e) {
            logger.info(e.getMessage());
        }
        throw new UserNotCreatedException("User was not created successfully");
    }

    @Override
    public User findUserByUsername(String username) {
        Connection connection = connectionUtil.getConnection();
        try {
            PreparedStatement prepStatement = connection.prepareStatement("select * from \"user\" where \"user\".username = ?;");
            prepStatement.setString(1, username);
            ResultSet results = prepStatement.executeQuery();
            if(results.next()) {
                return new User(results.getInt("user_id"), results.getString("username"));
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        throw new UserNotFoundException("User was not found");
    }

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        Connection conn = connectionUtil.getConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("select user_id, username from bank.user where username = ? and password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet results = preparedStatement.executeQuery();
            if(results.next()) {
                return new User(results.getInt("user_id"), results.getString("username"));
            }
        } catch(SQLException e) {
            logger.info(e.getMessage());
        }
        throw new RuntimeException("Sorry that username and/or password is incorrect.");
    }
}
