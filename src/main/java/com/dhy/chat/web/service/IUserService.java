package com.dhy.chat.web.service;

import com.dhy.chat.dto.user.AuthDto;
import com.dhy.chat.dto.user.CreateUserDto;
import com.dhy.chat.dto.user.LoginDto;
import com.dhy.chat.dto.user.UserDto;

import java.util.List;
import java.util.Set;

/**
 * @author vghosthunter
 */
public interface IUserService {

    /**
     * getUserByUsername
     * @param username username
     * @return UserDto
     */
    UserDto getUserByUsername(String username);

    /**
     * createUser
     * @param createUserDto createUserDto
     * @return UserDto
     */
    UserDto createUser(CreateUserDto createUserDto);

    /**
     * getById
     * @param id
     * @return UserDto
     */
    UserDto getById(String id);

    /**
     * updateClientId
     * @param id id
     * @param clientId clientId
     */
    void updateClientId(String id, String clientId);

    /**
     * getUserClientIds
     * @param userIds userIds
     * @return ClientIds
     */
    Set<String> getUserClientIds(List<String> userIds);

    /**
     * 用户登录
     * @param input
     * @return
     */
    AuthDto login(LoginDto input);

    /**
     * refreshToken
     * @param token
     * @param refreshToken
     * @return
     */
    AuthDto refreshToken(String token, String refreshToken);
}
