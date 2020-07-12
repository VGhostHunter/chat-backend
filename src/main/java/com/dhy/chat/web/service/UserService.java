package com.dhy.chat.web.service;

import com.dhy.chat.ChatAppSeeder;
import com.dhy.chat.dto.CreateUserDto;
import com.dhy.chat.dto.UserDto;
import com.dhy.chat.entity.Authority;
import com.dhy.chat.entity.User;
import com.dhy.chat.exception.BusinessException;
import com.dhy.chat.web.repository.AuthorityRepository;
import com.dhy.chat.web.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author vghosthunter
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    public UserDto createUser(CreateUserDto createUserDto) {
        User isExist = userRepository.findByUsername(createUserDto.getUsername());
        if(isExist != null) {
            throw new BusinessException("用户已经存在");
        }
        User user = new User();
        BeanUtils.copyProperties(createUserDto, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<GrantedAuthority> defaultRole = new ArrayList();
        Authority auth = authorityRepository.findByAuthority(ChatAppSeeder.GENERAL_USER);
        defaultRole.add(auth);
        user.setAuthorities(defaultRole);
        userRepository.save(user);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }
}
