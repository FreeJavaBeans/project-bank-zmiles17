package com.revature.repository;

import com.revature.model.Employee;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAO implements EmployeeRepository{

    private ConnectionUtil singleton = ConnectionUtil.getSingleton();

    @Override
    public Employee findEmployeeByUsernameAndPassword(String username, String password) {
        Connection conn = singleton.getConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("select * from employee where username = ? and password = ?;");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return new Employee(resultSet.getString("username"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred during employee login");
        }
        return null;
    }
}
