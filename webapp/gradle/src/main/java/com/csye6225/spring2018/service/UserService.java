package com.csye6225.spring2018.service;

import com.csye6225.spring2018.entity.User;

import javax.transaction.Transactional;

public interface UserService {
    User save(User user);

    boolean findUserbyUsername(String username);

    boolean checkAccount(String username, String password);

    //boolean findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
}
