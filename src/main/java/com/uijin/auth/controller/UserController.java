package com.uijin.auth.controller;

import com.uijin.auth.model.UserModel;
import com.uijin.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public UserModel.UserResponse postUser(@RequestBody UserModel.UserRequest userRequest) {
        return userService.addUser(userRequest);
    }

    @GetMapping("/{userId}")
    public UserModel.UserResponse getUser(@PathVariable long userId) {
        return userService.getUserInfo(userId);
    }
}
