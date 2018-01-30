package com.csye6225.spring2018.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(generator = "incrementGenerator",strategy = GenerationType.IDENTITY)
    @GenericGenerator(name="incrementGenerator",strategy = "increment")
    private long id;
    private String email;
    private String password;



    public User(){

    }


    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
