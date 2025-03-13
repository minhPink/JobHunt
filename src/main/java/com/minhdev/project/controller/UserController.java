package com.minhdev.project.controller;

import com.minhdev.project.domain.User;
import com.minhdev.project.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable long id) {
        User devUser = this.userService.handleGetUser(id);
        return devUser;
    }

    @GetMapping("/user")
    public List<User> getUsers() {
        return this.userService.handleGetAllUsers();
    }
    @PostMapping("/user")
    public User createNewUser(@RequestBody User request) {
        User devUser = this.userService.handleCreateUser(request);
        return devUser;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable long id) {
        this.userService.handleDeleteUser(id);
        return "User deleted";
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User request) {
        return this.userService.handleUpdateUser(request);
    }
}
