package com.dhy.chat.web.controller;

import com.dhy.chat.dto.CreateUserDto;
import com.dhy.chat.dto.Result;
import com.dhy.chat.dto.UserDto;
import com.dhy.chat.web.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author vghosthunter
 */
@RestController
@RequestMapping("/user")
@Api("User")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @ApiOperation("Create User")
    private Result<UserDto> create(@Valid @RequestBody CreateUserDto createUserDto) {
        UserDto userDto = userService.createUser(createUserDto);
        return Result.succeeded(userDto);
    }

    @GetMapping
    @ApiOperation("Get User")
    private Result<UserDto> get(@RequestParam String username) {
        UserDto userDto = userService.getUserByUsername(username);
        return Result.succeeded(userDto);
    }

    /**
     * 只是为了生成api
     * @param username
     * @param password
     */
    @PostMapping("/login")
    @ApiOperation("User Login")
    private void login(@RequestParam() String username, @RequestParam String password) {
    }
}
