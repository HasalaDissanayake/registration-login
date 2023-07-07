package com.example.registrationlogin.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailService {

    void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException;

}
