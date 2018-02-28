package com.csye6225.spring2018.repository;


import com.csye6225.spring2018.entity.User;
import org.springframework.data.repository.CrudRepository;


import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {


}
