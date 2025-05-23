package com.minhdev.project.repository;

import com.minhdev.project.domain.Company;
import com.minhdev.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    User findByRefreshTokenAndEmail(String refresh_token, String email);
    List<User> findAllByCompany(Company company);
}
