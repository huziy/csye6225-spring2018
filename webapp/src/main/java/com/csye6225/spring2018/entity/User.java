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
    private String photo;
    private String aboutMe;


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

    public String getPhoto(){return photo;}

    public void setPhoto(String photo){this.photo=photo;}

    public String getAboutMe(){return aboutMe;}

    public void setAboutMe(String aboutMe){this.aboutMe=aboutMe;}

}
