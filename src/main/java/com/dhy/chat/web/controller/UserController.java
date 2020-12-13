package com.dhy.chat.web.controller;

import com.dhy.chat.dto.user.*;
import com.dhy.chat.dto.Result;
import com.dhy.chat.utils.LocalMessageUtil;
import com.dhy.chat.web.service.user.IUserService;
import com.dhy.chat.web.service.user.impl.IUserCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author vghosthunter
 */
@RestController
@RequestMapping("/api/user")
@Api("User")
public class UserController {

    private final IUserService userService;
    private final LocalMessageUtil messageUtil;
    private final IUserCacheService userCacheService;

    public UserController(IUserService userService,
                          LocalMessageUtil messageUtil,
                          IUserCacheService userCacheService) {
        this.userService = userService;
        this.messageUtil = messageUtil;
        this.userCacheService = userCacheService;
    }


    @PostMapping("/create")
    @ApiOperation("Create User")
    private Result<UserDto> create(@Validated @RequestBody CreateUserDto createUserDto) {
        UserDto userDto = userService.createUser(createUserDto);
        return Result.succeeded(userDto);
    }

    @GetMapping
    @ApiOperation("Get User By Username")
    private Result<UserDto> get(@RequestParam String username) {
        UserDto userDto = userService.getUserByUsername(username);
        return Result.succeeded(userDto);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get User By Id")
    private Result<UserDto> getById(@PathVariable String id) {
        UserDto userDto = userService.getById(id);
        return Result.succeeded(userDto);
    }

    @PutMapping("/{id}/clientId")
    @ApiOperation("更新ClientId")
    private Result updateClientId(@PathVariable String id, @RequestParam String clientId) {
        userService.updateClientId(id, clientId);
        return Result.succeeded();
    }

    /**
     * 获取token
     */
    @PostMapping("/token")
    @ApiOperation("User Login")
    private ResponseEntity<?> token(@Validated @RequestBody LoginDto input) {
        return userService.getOptionalByUsernameAndPassword(input)
                .map(user -> {
                    if(user.isUsingMfa()) {
                        // 使用多因子认证
                        var mfaId = userCacheService.cacheUser(user);
                        return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .header("X-Authenticate", "mfa", "realm=" + mfaId)
                                .build();
                    } else {
                        return ResponseEntity.ok().body(userService.login(input));
                    }
                }).orElseThrow(() -> new BadCredentialsException(messageUtil.GetMsg("message.usernameOrPasswordError")));
    }

    @PostMapping("/totp")
    public void sendTotp(@Valid @RequestBody SendTotpDto sendTotpDto) {
        userService.sendTotp(sendTotpDto);
    }

    @PostMapping("/verify")
    public Result<AuthDto> verifyTotp(@Valid @RequestBody TotpVerificationDto input) {
        return Result.succeeded(userService.loginWithTotp(input));
    }

    /**
     * refresh token
     */
    @PostMapping("/token/refresh")
    @ApiOperation("refresh token ")
    private Result<AuthDto> refresh(@RequestHeader(name = "Authorization") String token,
                                    @RequestParam String refreshToken) {
        return Result.succeeded(userService.refreshToken(token, refreshToken));
    }
}
