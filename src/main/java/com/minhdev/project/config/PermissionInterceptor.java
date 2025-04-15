package com.minhdev.project.config;

import com.minhdev.project.domain.Permission;
import com.minhdev.project.domain.Role;
import com.minhdev.project.domain.User;
import com.minhdev.project.service.UserService;
import com.minhdev.project.util.SecurityUtil;
import com.minhdev.project.util.error.CustomizeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;


public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Transactional
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws CustomizeException {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path=" + path);
        System.out.println(">>> httpMethod=" + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get() : "";

        if (email != null && !email.isEmpty()) {
            User user = this.userService.handleGetUserByUsername(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission>permissions = role.getPermissions();
                    boolean isAllow = permissions.stream()
                            .anyMatch(item -> item.getApiPath().equals(path) && item.getMethod().equals(httpMethod));

                    if (isAllow == false) {
                        throw new CustomizeException("You have not allowed to access this resource");
                    }
                } else {
                    throw new CustomizeException("You have not allowed to access this resource");
                }
            }
        }
        return true;
        }
}