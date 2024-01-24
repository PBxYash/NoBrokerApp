package com.nobroker.service.impl;

import com.nobroker.entity.User;
import com.nobroker.payload.UserDto;
import com.nobroker.reposiotry.UserReposioty;
import com.nobroker.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserReposioty userReposioty;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = mapTOEntity(userDto);
        User save = userReposioty.save(user);
        UserDto userDto1 = mapToDto(save);
        return userDto1;
    }

    @Override
    public User getUserByEmail(String email) {
        return userReposioty.findByEmail(email);
    }

    @Override
    public void verityOtp(User user) {
        user.setEmailVerified(true);
        userReposioty.save(user);
    }

    @Override
    public boolean verifyEmail(String email) {
        User byEmail = userReposioty.findByEmail(email);
        return byEmail !=null && byEmail.isEmailVerified() ;
    }

    User mapTOEntity(UserDto userDto){
        User user1 = modelMapper.map(userDto, User.class);
        return user1;
    }
    UserDto mapToDto(User user){
        UserDto dto = modelMapper.map(user, UserDto.class);
        return dto;
    }
}
