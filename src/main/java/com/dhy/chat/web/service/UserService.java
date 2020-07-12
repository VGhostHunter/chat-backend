package com.dhy.chat.web.service;

import com.dhy.chat.dto.CreateUserDto;
import com.dhy.chat.dto.UserDto;
import com.dhy.chat.entity.User;
import com.dhy.chat.web.dao.UserDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserDto getUserById(String id) {
        Optional<User> byId = userDao.findById(id);
        if(!byId.isPresent()) {
            throw new EntityNotFoundException();
        }
        User user = byId.get();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    public UserDto createUser(CreateUserDto createUserDto) {
        User user = new User();
        BeanUtils.copyProperties(createUserDto, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }
}
