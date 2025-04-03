package com.minhdev.project.service;

import com.minhdev.project.domain.User;
import com.minhdev.project.domain.dto.Meta;
import com.minhdev.project.domain.dto.ResCreateUserDTO;
import com.minhdev.project.domain.dto.ResultPaginationDTO;
import com.minhdev.project.domain.dto.UpdateUserDTO;
import com.minhdev.project.repository.UserRepository;
import com.minhdev.project.util.error.CustomizeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleGetUser(long id) {
        return this.userRepository.findById(id).orElse(null);
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
        rs.setResult(pageUser.getContent());

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

    public User handleUpdateUser(UpdateUserDTO updateUserDTOUser) throws CustomizeException {
        long id = updateUserDTOUser.getId();
        User updateUser = this.userRepository.findById(id).orElse(null);
        if (updateUser == null) {
            throw new CustomizeException("User does not exist");
        }

        updateUser.setName(updateUserDTOUser.getName());
        updateUser.setGender(updateUserDTOUser.getGender());
        updateUser.setAge(updateUserDTOUser.getAge());
        updateUser.setAddress(updateUserDTOUser.getAddress());

        return updateUser;
    }



    public User handleGetUserByUsername(String email) {
        return this.userRepository.findByEmail(email);
    }

    public Boolean handleExistsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public  Boolean handleExistsById(long id) {
        return this.userRepository.existsById(id);
    }

    public User handleGetUserById(long id) {
        return this.userRepository.findById(id).orElse(null);
    }
}
