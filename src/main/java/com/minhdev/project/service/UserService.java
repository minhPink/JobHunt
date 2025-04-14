package com.minhdev.project.service;

import com.minhdev.project.domain.Company;
import com.minhdev.project.domain.Role;
import com.minhdev.project.domain.User;
import com.minhdev.project.domain.response.user.ResCreateUserDTO;
import com.minhdev.project.domain.response.user.ResUpdateUserDTO;
import com.minhdev.project.domain.response.user.ResUserDTO;
import com.minhdev.project.domain.response.ResultPaginationDTO;
import com.minhdev.project.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, CompanyService companyService, RoleService roleService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
        this.roleService = roleService;
    }

    public User handleGetUser(long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public ResUserDTO convertToResUserToDTO(User user) {
        ResUserDTO resUserDTO = new ResUserDTO();
        ResUserDTO.CompanyUser companyUserDTO = new ResUserDTO.CompanyUser();
        ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();

        if (user.getCompany() != null) {
            companyUserDTO.setId(user.getCompany().getId());
            companyUserDTO.setName(user.getCompany().getName());
            resUserDTO.setCompanyUser(companyUserDTO);
        }

        if (user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            resUserDTO.setRoleUser(roleUser);
        }

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
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        List<ResUserDTO> listUser = pageUser.getContent().stream().map(this::convertToResUserToDTO).toList();

        rs.setResult(listUser);

        return rs;
    }

    public User handleCreateUser(User user) {

        if (user.getCompany() != null) {
            Optional<Company> companyOptional = this.companyService.getCompany(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() == true ? companyOptional.get() : null);
        }

        if (user.getRole() != null) {
            Role role = this.roleService.findById(user.getRole().getId());
            user.setRole(role != null ? role : null);
        }
        return this.userRepository.save(user);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO resUser = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser companyUser = new ResCreateUserDTO.CompanyUser();
        ResCreateUserDTO.RoleUser roleUser = new ResCreateUserDTO.RoleUser();

        resUser.setId(user.getId());
        resUser.setName(user.getName());
        resUser.setEmail(user.getEmail());
        resUser.setAge(user.getAge());
        resUser.setGender(user.getGender());
        resUser.setAddress(user.getAddress());
        resUser.setCreatedAt(user.getCreatedAt());

        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            resUser.setCompanyUser(companyUser);
        }

        if (user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            resUser.setRoleUser(roleUser);
        }

        return resUser;
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User handleUpdateUser(User user) {
        if (user.getCompany() != null) {
            Optional<Company> companyOptional = this.companyService.getCompany(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() == true ? companyOptional.get() : null);
        }

        if (user.getRole() != null) {
            Role role = this.roleService.findById(user.getRole().getId());
            user.setRole(role != null ? role : null);
        }

        Optional<User> existUser = this.userRepository.findById(user.getId());
            User updateUser = existUser.get();
            updateUser.setName(user.getName());
            updateUser.setAge(user.getAge());
            updateUser.setGender(user.getGender());
            updateUser.setAddress(user.getAddress());
            updateUser.setCompany(user.getCompany());
            updateUser.setRole(user.getRole());
            return this.userRepository.save(updateUser);
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO resUser = new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyUser companyUser = new ResUpdateUserDTO.CompanyUser();
        ResUpdateUserDTO.RoleUser roleUser = new ResUpdateUserDTO.RoleUser();

        resUser.setId(user.getId());
        resUser.setName(user.getName());
        resUser.setEmail(user.getEmail());
        resUser.setAge(user.getAge());
        resUser.setGender(user.getGender());
        resUser.setAddress(user.getAddress());
        resUser.setUpdatedAt(user.getUpdatedAt());

        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            resUser.setCompanyUser(companyUser);
        }

        if (user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            resUser.setRoleUser(roleUser);
        }

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

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
