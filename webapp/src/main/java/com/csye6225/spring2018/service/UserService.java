package com.csye6225.spring2018.service;

import com.csye6225.spring2018.entity.User;
import org.springframework.stereotype.Service;

public interface UserService {

    User save(User user);
    boolean findByUsername(String username);
    boolean findByUsernameAndPassword(String username, String password);
    User getUser(String username);
}
