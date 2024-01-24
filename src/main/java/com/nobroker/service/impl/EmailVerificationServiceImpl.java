package com.nobroker.service.impl;

import com.nobroker.entity.User;
import com.nobroker.service.EmailService;
import com.nobroker.service.UserService;
import com.nobroker.service.EmailVerificationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);




    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailService emailService;

    static final Map<String, String> emailOtpMaping = new HashMap<>();

    private String lastUsedEmail;

    @Override
    public Map<String, String> verifyOtp(String email, String otp) {
        String storedOtp = emailOtpMaping.get(email);
        Map<String, String> response = new HashMap<>();
        if (storedOtp != null && storedOtp.equals(otp)) {
            User user = userService.getUserByEmail(email);
            if (user != null) {
                userService.verityOtp(user);
                emailOtpMaping.remove(email);
                response.put("status", "success");
                response.put("Message", "email verify Successfully");
            } else {
                response.put("status", "Error !!");
                response.put("Message", "User not found  !!!");
            }
        } else {
            response.put("status", "Error !!");
            response.put("Message", "Invalid Otp !!!!  !!!");
        }
        return response;
    }

    @Override
    public Map<String, String> sendOtpForLogin(String email) {
        Map<String, String> response = new HashMap<>();
        if (userService.verifyEmail(email)) {
            String otp = emailService.genrateOtp();
            emailOtpMaping.put(email, otp);
            sendEmail(email, "OTP for login verification", "Your OTP is " + otp);
            lastUsedEmail = email;
            response.put("status", "success");
            response.put("Message", " Otp send Successfully");
        } else {
            response.put("status", "Error !!");
            response.put("Message", "Invalid email !!!!  !!!");
        }

        return response;
    }

    @Override
    public Map<String, String> verifyOtpForLogin(String email, String otp) {
        Map<String, String> response = new HashMap<>();
        String stored = emailOtpMaping.get(email);
        if (stored != null && stored.equals(otp)) {
            emailOtpMaping.remove(email);
            response.put("status", "success");
            response.put("Message", "Login verify Successfully");
        } else {
            response.put("status", "Error");
            response.put("Message", "Invalid Otp !!!");
        }
        return response;
    }
//    @Scheduled(fixedRate = 1 * 60 * 1000)
//    private void scheduleOtpExpiration(String email) {
//       emailOtpMaping.remove(email);
//
//        }
        @Scheduled(fixedRate = 2 * 60 * 1000)
        private void scheduleOtpExpiration() {
        emailOtpMaping.remove(lastUsedEmail);
         }


    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

}
