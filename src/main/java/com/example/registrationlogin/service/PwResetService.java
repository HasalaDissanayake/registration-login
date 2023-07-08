package com.example.registrationlogin.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface PwResetService {

    void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException;

    void sendSMS(String recipientPhoneNumber, String messageBody);
}
