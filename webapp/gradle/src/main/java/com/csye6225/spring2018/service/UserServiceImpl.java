package com.csye6225.spring2018.service;


import com.csye6225.spring2018.entity.User;
import com.csye6225.spring2018.repository.UserRepository;
import com.csye6225.spring2018.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    public User save(User user) {
        //System.out.println(user);
        User u = userRepository.save(user);
        return u;
    }


    public boolean findUserbyUsername(String username) {
        User user = new User(username);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withIgnorePaths("id").withIgnorePaths("password");
        Example<User> u = Example.of(user, matcher);
        List<User> userList = userRepository.findAll(u);
        if (!userList.isEmpty()) return true;
        else return false;
    }


    public boolean checkAccount(String username, String password) {
        User user = new User(username, password);
        //List<String> ignoredAttriutes = Arrays.asList("id");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("password", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withIgnorePaths("id");
        Example<User> u = Example.of(user, matcher);
        List<User> userList = userRepository.findAll(u);
        if (!userList.isEmpty()) return true;
        else return false;
    }


    public User findByUsername(String username) {
        User user = new User(username);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withIgnorePaths("id").withIgnorePaths("password");
        Example<User> u = Example.of(user, matcher);
        User ur = userRepository.findOne(u);
        return ur;
    }
}
