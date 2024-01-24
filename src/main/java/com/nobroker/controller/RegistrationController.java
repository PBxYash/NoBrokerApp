package com.nobroker.controller;

import com.nobroker.payload.UserDto;
import com.nobroker.service.EmailService;
import com.nobroker.service.EmailVerificationService;
import com.nobroker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RegistrationController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @PostMapping("/register")
    public Map<String,String> createUsser(@RequestBody UserDto userDto){
        UserDto user = userService.createUser(userDto);
        return emailService.sendOtpEmail(userDto.getEmail());

    }
    @PostMapping("/verify-email")
    public Map<String,String> verifyEmail(@RequestParam String email ,@RequestParam String otp){
    return emailVerificationService.verifyOtp(email,otp);
    }

}
