package com.revature.repository;

import com.revature.model.User;

public interface UserRepository {

    public User createUser(String username, String password);

    public User findUserByUsername(String username);


}
