package com.example.registrationlogin.service.impl;

import com.example.registrationlogin.service.PwResetService;
import com.twilio.exception.ApiException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.UnsupportedEncodingException;

@Service
public class PwResetServiceImpl implements PwResetService {

    public final JavaMailSender mailSender;

    private String TWILIO_SID = "ACc68a37f31c2355ba8d31befba02495c4";

    private String TWILIO_AUTH = "4b45ee1a4fc7ecd7ed1d316de7150919";

    public PwResetServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("medease.lk@gmail.com", "MedEase Support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<html>"
                + "<body>"
                + "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=" + link + ">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>"
                + "</body>"
                + "</html>";

        helper.setSubject(subject);
        helper.setText(content,true);
        mailSender.send(message);

    }

    public void sendSMS(String recipientPhoneNumber, String link){

        Twilio.init(TWILIO_SID, TWILIO_AUTH);

        String messageBody = "Hello, Follow this  link to reset your password: "
                            + link + "\n\nIgnore this SMS if you have not made the request.";

        try {
            Message.creator(
                    new PhoneNumber(recipientPhoneNumber),
                    new PhoneNumber("+12176694144"),
                    messageBody
            ).create();
        } catch (ApiException e){
            System.err.println("Error Sending SMS: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error Sending SMS: " + e.getMessage());
        }
    }
}
