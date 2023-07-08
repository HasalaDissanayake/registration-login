package com.example.registrationlogin.controller;

import com.example.registrationlogin.dto.UserDto;
import com.example.registrationlogin.entity.User;
import com.example.registrationlogin.service.UserService;
import com.example.registrationlogin.service.PwResetService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@Controller
public class AuthController {

    private UserService userService;

    private PwResetService pwResetService;

    @Value("${app.base-url}")
    private String baseURL;

    public AuthController(UserService userService, PwResetService pwResetService) {
        this.userService = userService;
        this.pwResetService = pwResetService;
    }

    @GetMapping("/index")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(){
        return "forgot_password";
    }

    @PostMapping ("/forgot_password")
    public String processForgotPasswordForm(@RequestParam("email") String email, Model model){
        User existingUser = userService.findUserByEmail(email);

        if(existingUser == null){
            model.addAttribute("error","User with the provided email does not exist");
        } else {
            String token = UUID.randomUUID().toString();

            try {
                userService.updateResetPasswordtoken(token, email);
                String resetPasswordLink = baseURL + "/reset_password?token=" + token;
                pwResetService.sendEmail(email, resetPasswordLink);
                pwResetService.sendSMS("+94767256838",resetPasswordLink);
                model.addAttribute("message", "We have sent a reset password link to your email and mobile number. Please check.");
            } catch (UnsupportedEncodingException | MessagingException e ) {
                model.addAttribute("error", "Error while sending password reset link.");
            }
        }

        return "forgot_password";
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model){

        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if(user == null){
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "reset_password";

    }

    @PostMapping("/reset_password")
    public String processResetPasswordForm(@RequestParam("token") String token, @RequestParam("password") String password, Model model){
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if(user == null){
            model.addAttribute("message", "Invalid Token");
            return "reset_password";
        } else {
            userService.updatePassword(user, password);

            model.addAttribute("message", "You have successfully changed your password");
        }

        return "reset_password";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String listRegisteredUsers(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
}
