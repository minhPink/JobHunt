package com.minhdev.project.controller;

import com.minhdev.project.domain.User;
import com.minhdev.project.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/user/create")
    @PostMapping("/user/create")
    public User createNewUser(@RequestBody User request) {
        User devUser = this.userService.handleCreateUser(request);
        return devUser;
    }
}
