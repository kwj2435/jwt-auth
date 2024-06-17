package com.uijin.auth.controller;

import com.uijin.auth.model.UserModel;
import com.uijin.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserModel.UserResponse postUser(@RequestBody UserModel.UserRequest userRequest) {
        return userService.addUser(userRequest);
    }
}
