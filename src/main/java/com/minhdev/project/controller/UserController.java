package com.minhdev.project.controller;

import com.minhdev.project.domain.User;
import com.minhdev.project.domain.dto.ResCreateUserDTO;
import com.minhdev.project.domain.dto.ResultPaginationDTO;
import com.minhdev.project.service.UserService;
import com.minhdev.project.util.annotation.ApiMessage;
import com.minhdev.project.util.error.CustomizeException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
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

    @ApiMessage("Fetch all users")
    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getUsers(
            @Filter Specification<User> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllUsers(spec, pageable));
    }

    @ApiMessage("Create a new user")
    @PostMapping("/users")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User request) throws CustomizeException {
        String email = request.getEmail();
        Boolean existsUser = this.userService.handleExistsByEmail(email);

        if (existsUser) {
            throw new CustomizeException("Email already exists");
        };

        String hashedPassword = this.passwordEncoder.encode(request.getPassword());
        request.setPassword(hashedPassword);
        User devUser = this.userService.handleCreateUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(devUser));
    }

    @ApiMessage("Delete user success")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) throws CustomizeException {
        User existingUser = this.userService.handleGetUser(id);
        if (existingUser == null) {
            throw new CustomizeException("User does not exist");
        }

        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

//    @PutMapping("/users")
//    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody UpdateUserDTO request) throws IdInvalidException{
//
//
//        ResUpdateUserDTO resUser = new ResUpdateUserDTO();
//        resUser.setId(id);
//        resUser.setName(request.getName());
//        resUser.setAge(request.getAge());
//        resUser.setGender(request.getGender());
//        resUser.setAddress(request.getAddress());
//        resUser.setUpdatedAt(existsUser.getUpdatedAt());
//        return ResponseEntity.status(HttpStatus.OK).body(resUser);
//    }
}
