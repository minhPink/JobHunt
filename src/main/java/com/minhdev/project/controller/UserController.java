package com.minhdev.project.controller;

import com.minhdev.project.domain.User;
import com.minhdev.project.service.UserService;
import com.minhdev.project.service.error.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        User devUser = this.userService.handleGetUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(devUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllUsers());
    }
    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User request) {
        User devUser = this.userService.handleCreateUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(devUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("Id khong duoc lon hon 1500");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleUpdateUser(request));
    }
}
