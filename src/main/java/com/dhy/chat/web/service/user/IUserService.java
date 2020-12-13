package com.dhy.chat.web.service.user;

import com.dhy.chat.dto.user.*;
import com.dhy.chat.entity.User;

import java.util.List;
import java.util.Optional;
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
     * getOptionalByUsernameAndPassword
     * @param input input
     * @return Optional<UserDto>
     */
    Optional<User> getOptionalByUsernameAndPassword(LoginDto input);

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

    /**
     * sendTotp
     * @param sendTotpDto
     */
    void sendTotp(SendTotpDto sendTotpDto);

    /**
     * loginWithTotp
     * @param input
     * @return
     */
    AuthDto loginWithTotp(TotpVerificationDto input);
}
