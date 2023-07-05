package com.example.registrationlogin.service;

import com.example.registrationlogin.dto.UserDto;
import com.example.registrationlogin.entity.User;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    void updateResetPasswordtoken(String token, String Email);

    User getByResetPasswordToken(String token);

    void updatePassword(User user, String newPassword);

    List<UserDto> findAllUsers();

}
