package com.csye6225.spring2018.entity;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    private Long id;

    //@NotEmpty(message = "Username is Empty")
    //@Pattern(regexp = "^.+@.+$", message = "Invalid Email Address")
    //@Length(min = 8, max = 25, message = "Invalid Email Address")
    @Column(name = "username")
    private String username;

    //@NotEmpty(message = "Password is Empty")
   // @Length(min = 2, message = "Password Length Less Than 2")
    //@Pattern(regexp = "^[A-Za-z0-9^%&,;=?$@]+$", message = "Invalid Password")
    @Column(name = "password")
    private String password;

    @Column(name = "picturePath")
    private String picturePath;

    @Column(name = "aboutMe")
    private String aboutMe;

    public User() {
    }

    ;

    public User(String username) {
        this.username = username;
    }

    public User(String username, String password) {
        this.id = generateID();
        this.username = username;
        this.password = password;
    }

    private long generateID() {
        return (long) (Math.random() * 10000000);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    @Override
    public String toString() {
        return this.id + " " + this.username + " " + this.password;
    }
}
