package com.nobroker.service.impl;

import com.nobroker.Repository.UserRepository;
import com.nobroker.entity.User;
import com.nobroker.service.ForgetPasswordService;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.HashMap;
import java.util.Map;

@Service
public class ForgetPasswordServiceImpl implements ForgetPasswordService {

    private final Map<String, String> emailOtpMap = new HashMap<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

//    @Value("${app.url}")
//    private String appUrl;

    @Override
    public void forgotPassword(String userEmail) {
        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        // Generate a unique OTP
        String otp = genrateOtp();

        // Store the OTP in the map with the email as the key
        emailOtpMap.put(userEmail, otp);

        // Send an email with the OTP
        sendOtpEmail(userEmail, otp);
    }

    @Override
    public void resetPassword(String userEmail, String otp, String newPassword) {
        // Retrieve the OTP from the map
        String storedOtp = emailOtpMap.get(userEmail);

        if (storedOtp == null || !storedOtp.equals(otp)) {
            throw new NotFoundException("Invalid OTP");

        }

        // Remove the OTP from the map to ensure it can be used only once
        emailOtpMap.remove(userEmail);

        // Update the password
        User byEmail = userRepository.findByEmail(userEmail);

        if (byEmail == null) {
            throw new NotFoundException("User not found");
        }

        byEmail.setPassword(newPassword);

        // Save the updated user
        userRepository.save(byEmail);
    }

    private void sendOtpEmail(String userEmail, String otp) {
        // Construct the email message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("OTP for Password Reset");
        message.setText("Your OTP for password reset is: " + otp);

        // Send the email
        javaMailSender.send(message);
    }

    public String genrateOtp() {
        return String.format("%06d",new java.util.Random().nextInt(1000000));
    }
}
