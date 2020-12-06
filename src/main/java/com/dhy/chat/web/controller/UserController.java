package com.dhy.chat.web.controller;

import com.dhy.chat.dto.CreateUserDto;
import com.dhy.chat.dto.LoginDto;
import com.dhy.chat.dto.Result;
import com.dhy.chat.dto.UserDto;
import com.dhy.chat.web.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
     * 只是为了生成api
     */
    @PostMapping("/login")
    @ApiOperation("User Login")
    private void login(@Validated @RequestBody LoginDto input) {
    }
}
