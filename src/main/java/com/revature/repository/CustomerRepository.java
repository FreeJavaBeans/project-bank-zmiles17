package com.revature.repository;

import com.revature.model.Customer;
import com.revature.model.User;

public interface CustomerRepository {

    public Customer createCustomer(User user, int accountId);

    public Customer findCustomerByUserId(User user);
}
