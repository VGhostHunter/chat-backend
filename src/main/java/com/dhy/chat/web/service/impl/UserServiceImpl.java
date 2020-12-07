package com.dhy.chat.web.service.impl;

import com.dhy.chat.ChatAppSeeder;
import com.dhy.chat.dto.CreateUserDto;
import com.dhy.chat.dto.UserDto;
import com.dhy.chat.entity.Authority;
import com.dhy.chat.entity.User;
import com.dhy.chat.exception.BusinessException;
import com.dhy.chat.web.repository.AuthorityRepository;
import com.dhy.chat.web.repository.UserRepository;
import com.dhy.chat.web.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

/**
 * @author vghosthunter
 */
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto createUser(CreateUserDto createUserDto) {
        User isExist = userRepository.findByUsername(createUserDto.getUsername());
        if(isExist != null) {
            throw new BusinessException("message.usernameAlreadyExists");
        }
        User user = new User();
        BeanUtils.copyProperties(createUserDto, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Authority> defaultRole = new HashSet<>();
        Authority auth = authorityRepository.findByAuthority(ChatAppSeeder.GENERAL_USER);
        defaultRole.add(auth);
        user.setAuthorities(defaultRole);
        userRepository.save(user);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    @Override
    public UserDto getById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateClientId(String id, String clientId) {
        User user = userRepository.getOne(id);
        user.setClientId(clientId);
        userRepository.save(user);
    }

    @Override
    public Set<String> getUserClientIds(List<String> userIds) {
        Set<String> distinctClientIdByIdIn = userRepository.findDistinctClientIdByIdIn(userIds);
        return distinctClientIdByIdIn;
    }
}
