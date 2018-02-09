package com.csye6225.spring2018.controller;


import com.csye6225.spring2018.security.BCryptPasswordEncoderBean;
import com.csye6225.spring2018.entity.User;
import com.csye6225.spring2018.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
<<<<<<< HEAD
import javax.servlet.http.HttpSession;
=======
>>>>>>> 39e831c605a60a016302283af0287dfe71cb9e68
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserRepository userRepository;
    @GetMapping(path="/register")
    public @ResponseBody ModelAndView register1 (HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("register");
        return mav;
    }

    @PostMapping(path="/register")
<<<<<<< HEAD
    public @ResponseBody ModelAndView register (HttpServletRequest request,Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean flag = false;
=======
    public @ResponseBody ModelAndView register (HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean flag=false;
>>>>>>> 39e831c605a60a016302283af0287dfe71cb9e68
        List<User> userList = (List<User>) userRepository.findAll();
        ModelAndView mav = new ModelAndView();

<<<<<<< HEAD
        for (User a : userList) {
            System.out.println(a.getEmail()+""+a.getPassword());
=======
        for (User a: userList) {
>>>>>>> 39e831c605a60a016302283af0287dfe71cb9e68
            if (a.getEmail().equals(username)) {
                flag = true;
                break;
            }
        }
<<<<<<< HEAD

        if (flag) {
=======
        ModelAndView mav = new ModelAndView();
        if(flag){
>>>>>>> 39e831c605a60a016302283af0287dfe71cb9e68
            mav.setViewName("registerError");
            return mav;
        } else {

            User newuser = new User();
            BCryptPasswordEncoderBean bCryptPasswordEncoder = new BCryptPasswordEncoderBean();


<<<<<<< HEAD
            String passwordSafe = bCryptPasswordEncoder.bCryptPasswordEncoder().encode(password);
=======
            String passwordSafe= bCryptPasswordEncoder.bCryptPasswordEncoder().encode(password);
>>>>>>> 39e831c605a60a016302283af0287dfe71cb9e68
            newuser.setEmail(username);
            newuser.setPassword(passwordSafe);
            userRepository.save(newuser);
            mav.setViewName("login");
            return mav;
        }
    }

    @GetMapping(path="/loginSuccessful")
    public @ResponseBody ModelAndView login1 (HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }
<<<<<<< HEAD
=======
    @GetMapping(path="/loginSuccessful")
    public @ResponseBody ModelAndView login1 (HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }
>>>>>>> 39e831c605a60a016302283af0287dfe71cb9e68
    @PostMapping(path="/loginSuccessful")
    public @ResponseBody ModelAndView login ( HttpServletRequest request, Model model){


        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean flag=false;
        List<User> userList = (List<User>)userRepository.findAll();
        BCryptPasswordEncoderBean bCryptPasswordEncoder=new BCryptPasswordEncoderBean();
        for (User a: userList) {
            if (a.getEmail().equals(username)&&bCryptPasswordEncoder.bCryptPasswordEncoder().matches(password,a.getPassword())) {
                flag = true;
                break;
            }
        }
<<<<<<< HEAD
           ModelAndView mav = new ModelAndView();

           if(flag) {
               mav.setViewName("index");
               return mav;
           }else{
               mav.setViewName("loginError");
               return mav;
           }
=======
        ModelAndView mav = new ModelAndView();

        if(flag) {
            mav.setViewName("index");
            return mav;
        }else{
            mav.setViewName("loginError");
            return mav;
        }
>>>>>>> 39e831c605a60a016302283af0287dfe71cb9e68
    }

    @GetMapping(path="/logout")
    public @ResponseBody ModelAndView logout (HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
<<<<<<< HEAD
        HttpSession session=request.getSession();
        session.invalidate();
=======
>>>>>>> 39e831c605a60a016302283af0287dfe71cb9e68
        mav.setViewName("login");
        return mav;
    }
}
