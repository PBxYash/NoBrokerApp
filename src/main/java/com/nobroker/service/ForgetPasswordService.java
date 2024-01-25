package com.nobroker.service;

public interface ForgetPasswordService {

    void forgotPassword(String userEmail);

    void resetPassword(String userEmail, String otp, String newPassword);
}
