package com.revature.repository;

import com.revature.model.User;

public interface UserRepository {

    public User createUser(User user);

    public User findUser(String username, String password);


}
