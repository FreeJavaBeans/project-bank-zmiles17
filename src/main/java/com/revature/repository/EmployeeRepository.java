package com.revature.repository;

import com.revature.model.Employee;

public interface EmployeeRepository {

    public Employee findEmployeeByUsernameAndPassword(String username, String password);
}
