package com.nobroker.service;

import java.util.Map;

public interface EmailService {
    public String genrateOtp();
    Map<String ,String> sendOtpEmail(String email);
}
