package com.minhdev.project.controller;

import com.minhdev.project.domain.User;
import com.minhdev.project.domain.dto.ResultPaginationDTO;
import com.minhdev.project.service.UserService;
import com.minhdev.project.util.error.IdInvalidException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        User devUser = this.userService.handleGetUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(devUser);
    }

    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getUsers(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional ) {
        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
        Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1, Integer.parseInt(sPageSize));
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllUsers(pageable));
    }
    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User request) {
        String hashedPassword = this.passwordEncoder.encode(request.getPassword());
        request.setPassword(hashedPassword);
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
