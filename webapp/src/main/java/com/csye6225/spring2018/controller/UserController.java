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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.io.IOException;

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

    @PostMapping(value = "/uploadphoto")
    public ModelAndView uploadPicture(HttpSession session, HttpServletRequest request) throws IOException {

        String fromPicturePath=Path.extractPrefixPath + request.getParameter("photo");
        String toPicturePath = Path.savePrefixPath + request.getParameter("photo");
        String userPath = Path.applicationPrefixPath + request.getParameter("photo");
        Files.copy(Paths.get(fromPicturePath), Paths.get(toPicturePath), StandardCopyOption.REPLACE_EXISTING);
        final String username = session.getAttribute("username").toString();
        if (username.isEmpty()) {
            return new ModelAndView("index");
        }
        User user = userRepository.findByEmail(username);
        user.setPhoto(userPath);
        userRepository.save(user);
        ModelAndView mav = new ModelAndView();
        mav.addObject("username", username);
        mav.addObject("picturepath", userPath);
        mav.addObject("aboutMe", userRepository.findByEmail(username).getAboutMe());
        mav.setViewName("index");
        return mav;
    }

    @RequestMapping(value = "/deletephoto", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView deleteProfilePicture(HttpSession session) {
        final String username = session.getAttribute("user").toString();
        if (username.isEmpty()) {
            return new ModelAndView("index");
        }
        User user = userRepository.findByEmail(username);
        user.setPhoto(null);
        userRepository.save(user);
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", username);

        mav.addObject("picturepath", userRepository.findByEmail(username).getPhoto());
        mav.addObject("aboutMe", userRepository.findByEmail(username).getAboutMe());
        mav.setViewName("userindex");
        return mav;
    }

    @PostMapping(value = "/uploadaboutme")
    public ModelAndView uploadAboutMe(HttpSession session, HttpServletRequest request) {
        final String username = session.getAttribute("user").toString();
        if (username.isEmpty()) {
            return new ModelAndView("index");
        }
        final String aboutMe = request.getParameter("aboutme");
        User user = userRepository.findByEmail(username);
        user.setAboutMe(aboutMe);
        userRepository.save(user);
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", username);
        mav.addObject("picturepath", userRepository.findByEmail(username).getPhoto());
        mav.addObject("aboutMe", userRepository.findByEmail(username).getAboutMe());
        mav.setViewName("userindex");
        return mav;
    }

    @GetMapping(value = "/showprofile")
    public ModelAndView showProfile(@RequestParam("username") String username) {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            return new ModelAndView("loginError", "errorMessage", "User Not Exists");
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", username);
        mav.addObject("picturepath", user.getPhoto());
        mav.addObject("aboutme", user.getAboutMe());
        mav.setViewName("profile");
        return mav;
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
