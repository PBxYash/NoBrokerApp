package com.nobroker.service;

import com.nobroker.entity.User;
import com.nobroker.payload.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);

    User getUserByEmail(String email);

    void verityOtp(User user);

    boolean verifyEmail(String email);
}
