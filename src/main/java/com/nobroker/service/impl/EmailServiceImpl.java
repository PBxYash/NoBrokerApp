package com.nobroker.service.impl;

import com.nobroker.service.EmailService;
import com.nobroker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.nobroker.service.impl.EmailVerificationServiceImpl.emailOtpMaping;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private  JavaMailSender javaMailSender;

    private final UserService userService;

    public EmailServiceImpl(JavaMailSender javaMailSender, UserService userService) {
        this.javaMailSender = javaMailSender;
        this.userService = userService;
    }

    @Override
    public String genrateOtp() {
        return String.format("%06d",new java.util.Random().nextInt(1000000));
    }

    public Map<String ,String> sendOtpEmail(String email){
        String otp = genrateOtp();
        emailOtpMaping.put(email,otp);
        sendEmail(email,"OTP for email verification","Your OTP is "+otp);
        Map<String,String>response = new HashMap<>();
        response.put("Status","Sucess");
        response.put("message","Otp send Successfully");
        return response;
    }

    private void sendEmail(String to ,String subject,String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
