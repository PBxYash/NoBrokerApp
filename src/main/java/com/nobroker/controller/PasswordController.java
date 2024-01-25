package com.nobroker.controller;

import com.nobroker.service.ForgetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    @Autowired
    private ForgetPasswordService forgetPasswordService;

    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        forgetPasswordService.forgotPassword(email);
        return ResponseEntity.ok("OTP sent successfully to reset password");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {
        forgetPasswordService.resetPassword(email, otp, newPassword);
        return ResponseEntity.ok("Password reset successfully");
    }
}
