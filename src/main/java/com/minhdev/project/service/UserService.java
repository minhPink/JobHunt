package com.minhdev.project.service;

import com.minhdev.project.domain.User;
import com.minhdev.project.domain.dto.*;
import com.minhdev.project.repository.UserRepository;
import com.minhdev.project.util.error.CustomizeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleGetUser(long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public ResUserDTO convertUserToDTO(User user) {
        ResUserDTO resUserDTO = new ResUserDTO();
        resUserDTO.setId(user.getId());
        resUserDTO.setName(user.getName());
        resUserDTO.setEmail(user.getEmail());
        resUserDTO.setAge(user.getAge());
        resUserDTO.setGender(user.getGender());
        resUserDTO.setAddress(user.getAddress());
        resUserDTO.setCreatedAt(user.getCreatedAt());
        resUserDTO.setUpdatedAt(user.getUpdatedAt());

        return resUserDTO;
    }

    public ResultPaginationDTO handleGetAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        List<ResUserDTO> listUser = pageUser.getContent().stream().map(this::convertUserToDTO).toList();

        rs.setResult(listUser);

        return rs;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO resUser = new ResCreateUserDTO();
        resUser.setId(user.getId());
        resUser.setName(user.getName());
        resUser.setEmail(user.getEmail());
        resUser.setAge(user.getAge());
        resUser.setGender(user.getGender());
        resUser.setAddress(user.getAddress());
        resUser.setCreatedAt(user.getCreatedAt());

        return resUser;
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User handleUpdateUser(User user) {
        Optional<User> existUser = this.userRepository.findById(user.getId());
        if (existUser.isPresent()) {
            User updateUser = existUser.get();
            updateUser.setName(user.getName());
            updateUser.setAge(user.getAge());
            updateUser.setGender(user.getGender());
            updateUser.setAddress(user.getAddress());
            return this.userRepository.save(updateUser);
        }
        return null;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO resUser = new ResUpdateUserDTO();
        resUser.setId(user.getId());
        resUser.setName(user.getName());
        resUser.setEmail(user.getEmail());
        resUser.setAge(user.getAge());
        resUser.setGender(user.getGender());
        resUser.setAddress(user.getAddress());
        resUser.setUpdatedAt(user.getUpdatedAt());

        return resUser;
    }


    public User handleGetUserByUsername(String email) {
        return this.userRepository.findByEmail(email);
    }

    public Boolean handleExistsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User handleGetUserById(long id) {
        return this.userRepository.findById(id).orElse(null);
    }
}
