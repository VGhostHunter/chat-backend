package com.dhy.chat.web.controller;

import com.dhy.chat.dto.CreateUserDto;
import com.dhy.chat.dto.Result;
import com.dhy.chat.dto.UserDto;
import com.dhy.chat.dto.UserLoginDto;
import com.dhy.chat.web.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Api("User")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @ApiOperation("Create User")
    private Result<UserDto> create(@Valid @RequestBody CreateUserDto createUserDto) {
        UserDto userDto = userService.createUser(createUserDto);
        return Result.succeeded(userDto);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get User")
    private Result<UserDto> get(@PathVariable String id) {
        UserDto userDto = userService.getUserById(id);
        return Result.succeeded(userDto);
    }

    @PostMapping("/login")
    private void login(@RequestParam() String username, @RequestParam String password) {
    }
}
