package com.csye6225.spring2018.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.csye6225.spring2018.entity.User;
import com.csye6225.spring2018.config.AWSConfiguration;
import com.csye6225.spring2018.constant.ApplicationConstant;
import com.csye6225.spring2018.repository.UserRepository;
import com.csye6225.spring2018.service.UserService;
import com.csye6225.spring2018.security.BCryptPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.*;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/user")

public class UserController {

    @Resource
    private UserService userService;

    @Autowired
    private AWSConfiguration awsConfiguration;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/signin", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView signIn() {
        return new ModelAndView("signin");
    }

    @RequestMapping(value = "/signup", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView signUp() {
        return new ModelAndView("signup");
    }

    @RequestMapping(value = "/signinvalidate", method = { RequestMethod.POST })
    public ModelAndView signInValidate(@Valid User user, BindingResult result, HttpServletRequest request,
                                       HttpServletResponse response, Model model, HttpSession session) throws IOException {

        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            String eMessage = errors.stream().map(e -> e.getDefaultMessage()).findFirst().get();
            return new ModelAndView("errorpage", "errorMessage", eMessage);
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        boolean exists = userService.findUserbyUsername(username);
        if (!exists) {
            return new ModelAndView("errorpage", "errorMessage", "Account Not Found");
        }
        String enPassword = BCrypt.hashpw(password, BCryptPassword.SALT);
        boolean checked = userService.checkAccount(username, enPassword);
        if (!checked) {
            return new ModelAndView("errorpage", "errorMessage", "Username or Password Invalid");
        } else {
            byte[] userProfile = null;
            final AmazonS3 s3 = awsConfiguration.amazonS3Client(awsConfiguration.basicAWSCredentials());
            try {
                S3Object o = s3.getObject(ApplicationConstant.bucketName, username);
                S3ObjectInputStream s3is = o.getObjectContent();
                byte[] read_buf = new byte[1000];
                int n = 0;
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
                while ((n = s3is.read(read_buf)) != -1) {
                    bos.write(read_buf, 0, n);
                }
                userProfile = bos.toByteArray();
                bos.close();
            } catch (Exception e) {
                ModelAndView mav = new ModelAndView();
                mav.addObject("user", username);
                mav.addObject("currentTime", new Date().toString());
                mav.addObject("aboutMe", userService.findByUsername(username).getAboutMe());
                mav.setViewName("userindex");
                session.setAttribute("user", username);
                return mav;
            }
            byte[] encodeBase64 = Base64.getEncoder().encode(userProfile);
            String transUserProfile = new String(encodeBase64, "UTF-8");

            ModelAndView mav = new ModelAndView();
            mav.addObject("userProfile", transUserProfile);
            mav.addObject("user", username);
            mav.addObject("currentTime", new Date().toString());
            mav.addObject("aboutMe", userService.findByUsername(username).getAboutMe());
            mav.setViewName("userindex");
            session.setAttribute("user", username);
            return mav;
        }
    }
    @RequestMapping(value = "/signupvalidate", method = { RequestMethod.POST })
    public ModelAndView signUpValidate(@Valid User user, BindingResult result, HttpServletRequest request,
                                       Model model) {
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            String eMessage = errors.stream().map(e -> e.getDefaultMessage()).findFirst().get();
            return new ModelAndView("errorpage", "errorMessage", eMessage);
        }
        String password = user.getPassword();
        String enPassword = BCrypt.hashpw(password, BCryptPassword.SALT);
        User u = new User(request.getParameter("username"), enPassword);
        boolean exists = userService.findUserbyUsername(u.getUsername());
        if (exists) {
            return new ModelAndView("errorpage", "errorMessage", "Account Already Exists");
        }
        User uCheck = userService.save(u);
        if (uCheck != null) {
            return new ModelAndView("index");
        } else {
            return new ModelAndView("errorpage", "errorMessage", "Save User Failed");
        }

    }

    @RequestMapping(value = "/uploadpicture", method = RequestMethod.POST)
    public ModelAndView uploadProfilePicture(HttpSession session, HttpServletRequest request) throws IOException {

        final String username = session.getAttribute("user").toString();
        if (username.isEmpty()) {
            return new ModelAndView("index");
        }

        // s3
        final AmazonS3 s3 = awsConfiguration.amazonS3Client(awsConfiguration.basicAWSCredentials());
        s3.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).disableChunkedEncoding().build());
        byte[] userProfile = null;
        try {
            s3.putObject(new PutObjectRequest(ApplicationConstant.bucketName, username,
                   new File(ApplicationConstant.extractPrefixPath + request.getParameter("profilepicture"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            S3Object o = s3.getObject(ApplicationConstant.bucketName, username);
            S3ObjectInputStream s3is = o.getObjectContent();
            byte[] read_buf = new byte[1000];
            int n = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            while ((n = s3is.read(read_buf)) != -1) {
                bos.write(read_buf, 0, n);
            }
            userProfile = bos.toByteArray();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] encodeBase64 = Base64.getEncoder().encode(userProfile);
        String transUserProfile = new String(encodeBase64, "UTF-8");
        ModelAndView mav = new ModelAndView();
        User user = new User();
        List<User> userList = userRepository.findAll();
        for (User u : userList) {
            if (u.getUsername().equals(username)) {
                user = u;
                break;
            }
        }
        String picturePath = s3.getUrl("s3.csye6225-spring2018-huziy.me", username).toString();
        user.setPicturePath(picturePath);
        userRepository.save(user);
        mav.addObject("user", username);
        mav.addObject("currentTime", new Date().toString());
        mav.addObject("userProfile", transUserProfile);
        mav.addObject("aboutMe", userService.findByUsername(username).getAboutMe());
        mav.setViewName("userindex");
        return mav;
    }

    @RequestMapping(value = "/deletepicture", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView deleteProfilePicture(HttpSession session) {
        final String username = session.getAttribute("user").toString();
        if (username.isEmpty()) {
            return new ModelAndView("index");
        }
        User user = userService.findByUsername(username);
        user.setPicturePath(null);
        userService.save(user);
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", username);
        mav.addObject("currentTime", new Date().toString());
        mav.addObject("aboutMe", userService.findByUsername(username).getAboutMe());
        final AmazonS3 s3 = awsConfiguration.amazonS3Client(awsConfiguration.basicAWSCredentials());
        try {
            s3.deleteObject(ApplicationConstant.bucketName, username);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
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
        User user = userService.findByUsername(username);
        user.setAboutMe(aboutMe);
        userService.save(user);
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", username);
        mav.addObject("currentTime", new Date().toString());
        mav.addObject("picturepath", userService.findByUsername(username).getPicturePath());
        mav.addObject("aboutMe", userService.findByUsername(username).getAboutMe());
        mav.setViewName("userindex");
        return mav;
    }

    @GetMapping(value = "/showprofile")
    public ModelAndView showProfile(@RequestParam("username") String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return new ModelAndView("errorpage", "errorMessage", "User Not Exists");
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", username);
        mav.addObject("picturepath", user.getPicturePath());
        mav.addObject("aboutme", user.getAboutMe());
        mav.setViewName("profile");
        return mav;
    }

    @RequestMapping(value = "/logout", method = { RequestMethod.GET })
    public ModelAndView logOut(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        session.invalidate();
        return new ModelAndView("index");
    }

}
