package com.minhdev.project.controller;

import com.minhdev.project.domain.User;
import com.minhdev.project.domain.request.ReqLoginDTO;
import com.minhdev.project.domain.response.login.ResLoginDTO;
import com.minhdev.project.service.UserService;
import com.minhdev.project.util.SecurityUtil;
import com.minhdev.project.util.annotation.ApiMessage;
import com.minhdev.project.util.error.CustomizeException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    @Value("${minh.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil,
            UserService userService ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User existUser = this.userService.handleGetUserByUsername(loginDTO.getEmail());

        if (existUser != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    existUser.getId(),
                    existUser.getEmail(),
                    existUser.getName());
            resLoginDTO.setUser(userLogin);
        }

        String access_token = this.securityUtil.createAccessToken(authentication.getName(), resLoginDTO);
        resLoginDTO.setAccessToken(access_token);
        String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getEmail(), resLoginDTO);

        this.userService.updateUserToken(refresh_token,loginDTO.getEmail());

        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", refresh_token)
                .httpOnly(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(resLoginDTO);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        User existUser = this.userService.handleGetUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        if (existUser != null) {
            userLogin.setId(existUser.getId());
            userLogin.setEmail(existUser.getEmail());
            userLogin.setName(existUser.getName());
        }
        return ResponseEntity.ok().body(userLogin);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get new token user")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token") String refresh_token
    ) throws CustomizeException{
        Jwt decodeToken = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodeToken.getSubject();

        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if (currentUser == null) {
            throw new CustomizeException("Error...");
        }

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User existUser = this.userService.handleGetUserByUsername(email);

        if (existUser != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    existUser.getId(),
                    existUser.getEmail(),
                    existUser.getName());
            resLoginDTO.setUser(userLogin);
        }

        String access_token = this.securityUtil.createAccessToken(email, resLoginDTO);
        resLoginDTO.setAccessToken(access_token);
        String new_refresh_token = this.securityUtil.createRefreshToken(email, resLoginDTO);

        this.userService.updateUserToken(new_refresh_token, email);

        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(resLoginDTO);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logout server")
    public ResponseEntity<Void> logout() throws CustomizeException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";

        if (email.equals("")) {
            throw new CustomizeException("Error...");
        }

        this.userService.updateUserToken(null, email);

        ResponseCookie deleteCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(null);
    }
}
