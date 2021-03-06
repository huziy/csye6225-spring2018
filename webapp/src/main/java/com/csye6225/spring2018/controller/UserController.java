
package com.csye6225.spring2018.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.csye6225.spring2018.Utility.BCryptUtility;
import com.csye6225.spring2018.Utility.Base64Utility;
import com.csye6225.spring2018.Utility.PictureStoreUtility;
import com.csye6225.spring2018.config.AWSDefaultConfiguration;
import com.csye6225.spring2018.constant.ApplicationConstant;
import com.csye6225.spring2018.entity.User;
import com.csye6225.spring2018.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @GetMapping(value = "/signin")
    public ModelAndView signIn() {
        return new ModelAndView("signin");
    }

    @GetMapping(value = "/signup")
    public ModelAndView signup() {
        return new ModelAndView("signup");
    }

    @PostMapping(value = "/signupvalidate")
    public ModelAndView signUpValidate(@Valid User user, BindingResult result, HttpServletRequest request, Model model) {

        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            String eMessage = errors.stream().map(e -> e.getDefaultMessage()).findFirst().get();
            return new ModelAndView("errorpage", "errorMessage", eMessage);
        }

        String password = user.getPassword();
        String enPassword = BCrypt.hashpw(password, BCryptUtility.SALT);
        User u = new User(request.getParameter("username"), enPassword);
        final boolean exists = userService.findByUsername(u.getUsername());
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

    @PostMapping(value = "/signinvalidate")
    public ModelAndView siginInValidate(@Valid User user, BindingResult result, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {

        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            String eMessage = errors.stream().map(e -> e.getDefaultMessage()).findFirst().get();
            return new ModelAndView("errorpage", "errorMessage", eMessage);
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        final boolean exists = userService.findByUsername(username);
        if (!exists) {
            return new ModelAndView("errorpage", "errorMessage", "Account Not Found");
        }

        String enPassword = BCrypt.hashpw(password, BCryptUtility.SALT);
        final boolean checked = userService.findByUsernameAndPassword(username, enPassword);
        if (!checked) {
            return new ModelAndView("errorpage", "errorMessage", "Username or Password Invalid");
        }

        User loggedUser = userService.getUser(username);
        ModelAndView mav = new ModelAndView();
        mav.addObject("aboutMe", loggedUser.getAboutMe());
        mav.addObject("currentTime", LocalDate.now());
        mav.addObject("user", username);
        session.setAttribute("user", username);

        String envir = env.getProperty("profile");


            final AmazonS3 s3 = AWSDefaultConfiguration.getInstance();
            String profileData = null;
            try {
                S3Object o = s3.getObject(ApplicationConstant.bucket, username);
                profileData = Base64Utility.transferProfile(o);
            } catch (Exception e) {
                mav.setViewName("userindex");
                return mav;
            }
            mav.addObject("userProfile", profileData);
            mav.setViewName("userindex");
            return mav;
 
    }

    @PostMapping(value = "/uploadpicture")
    public ModelAndView uploadProfilePicture(HttpSession session, HttpServletRequest request, @RequestParam("profilepicture") MultipartFile file) throws Exception {
        final String username = session.getAttribute("user").toString();
        if (username.isEmpty()) {
            return new ModelAndView("index");
        }
        User loggedUser = userService.getUser(username);
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", username);
        mav.addObject("currentTime", LocalDate.now());
        mav.addObject("aboutMe", loggedUser.getAboutMe());

        String envir = env.getProperty("profile");


            System.out.println(file.getOriginalFilename());
            final AmazonS3 s3 = AWSDefaultConfiguration.getInstance();
            String profileData = null;
            System.out.println(file.getBytes());
            File x = PictureStoreUtility.convertFromMultipart(file);
            System.out.println(System.getProperty("user.dir"));
            System.out.println(x.getAbsolutePath());
            System.out.println(x.getName());
            try {
                s3.putObject(new PutObjectRequest(ApplicationConstant.bucket, username, x));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                profileData = Base64Utility.transferProfile(s3.getObject(ApplicationConstant.bucket, username));
            } catch (Exception e) {
                e.printStackTrace();
            }
            mav.addObject("userProfile", profileData);
            mav.setViewName("userindex");
            return mav;
        }


    @RequestMapping(value = "/deletepicture", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView deleteProfilePicture(HttpSession session) throws IOException {
        final String username = session.getAttribute("user").toString();
        if (username.isEmpty()) {
            return new ModelAndView("index");
        }
        User loggedUser = userService.getUser(username);
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", username);
        mav.addObject("currentTime", LocalDate.now());
        mav.addObject("aboutMe", loggedUser.getAboutMe());

        String envir = env.getProperty("profile");

            final AmazonS3 s3 = AWSDefaultConfiguration.getInstance();
            try {
                s3.deleteObject(ApplicationConstant.bucket, loggedUser.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mav.addObject("userProfile", null);
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
        User u = userService.getUser(username);
        u.setAboutMe(aboutMe);
        userService.save(u);

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", username);
        mav.addObject("currentTime", LocalDate.now());
        mav.addObject("aboutMe", u.getAboutMe());

        String envir = env.getProperty("profile");

            final AmazonS3 s3 = AWSDefaultConfiguration.getInstance();
            String profileData = null;
            try {
                S3Object o = s3.getObject(ApplicationConstant.bucket, username);
                profileData = Base64Utility.transferProfile(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mav.addObject("userProfile", profileData);
            mav.setViewName("userindex");
            return mav;
        }



    @GetMapping(value = "/showprofile")
    public ModelAndView showAboutme(@RequestParam("username") String username) {
        User user = userService.getUser(username);
        if (user == null) {
            return new ModelAndView("errorpage", "errorMessage", "User Not Exists");
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", username);
        mav.addObject("aboutme", user.getAboutMe());
        mav.setViewName("profile");
        return mav;
    }

    @RequestMapping(value = "/resetpassword", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView resetPassword(HttpServletRequest request,HttpSession session, @RequestParam("email") String email) {
        AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();
        String msg = request.getParameter("email");
        String topicArn = snsClient.createTopic("password_reset").getTopicArn();
        PublishRequest publishRequest = new PublishRequest(topicArn, msg);
        PublishResult publishResult = snsClient.publish(publishRequest);
        return new ModelAndView("index");
    }

    @GetMapping(value = "/logout")
    public ModelAndView logOut(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        if (!session.getAttribute("user").equals("")) {
            session.setAttribute("user", null);
        }
        session.invalidate();
        return new ModelAndView("index");
    }

}
