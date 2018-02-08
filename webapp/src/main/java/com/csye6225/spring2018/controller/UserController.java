package com.csye6225.spring2018.controller;


import com.csye6225.spring2018.security.BCryptPasswordEncoderBean;
import com.csye6225.spring2018.entity.User;
import com.csye6225.spring2018.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public @ResponseBody ModelAndView register (HttpServletRequest request,Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean flag = false;
        List<User> userList = (List<User>) userRepository.findAll();
        ModelAndView mav = new ModelAndView();

        for (User a : userList) {
            System.out.println(a.getEmail()+""+a.getPassword());
            if (a.getEmail().equals(username)) {
                flag = true;
                break;
            }
        }

        if (flag) {
            mav.setViewName("registerError");
            return mav;
        } else {

            User newuser = new User();
            BCryptPasswordEncoderBean bCryptPasswordEncoder = new BCryptPasswordEncoderBean();


            String passwordSafe = bCryptPasswordEncoder.bCryptPasswordEncoder().encode(password);
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
           ModelAndView mav = new ModelAndView();

           if(flag) {
               mav.setViewName("index");
               return mav;
           }else{
               mav.setViewName("loginError");
               return mav;
           }
    }

    @GetMapping(path="/logout")
    public @ResponseBody ModelAndView logout (HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        HttpSession session=request.getSession();
        session.invalidate();
        mav.setViewName("login");
        return mav;
    }
}
