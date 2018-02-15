package com.csye6225.spring2018.repository;


import com.csye6225.spring2018.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    public User findByEmail(String email);

}
