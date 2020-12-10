package com.dhy.chat.web.controller;

import com.dhy.chat.dto.user.AuthDto;
import com.dhy.chat.dto.user.CreateUserDto;
import com.dhy.chat.dto.user.LoginDto;
import com.dhy.chat.dto.Result;
import com.dhy.chat.dto.user.UserDto;
import com.dhy.chat.web.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author vghosthunter
 */
@RestController
@RequestMapping("/api/user")
@Api("User")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
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
    private Result<AuthDto> token(@Validated @RequestBody LoginDto input) {
        return Result.succeeded(userService.login(input));
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
