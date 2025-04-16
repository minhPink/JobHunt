package com.minhdev.project.controller;

import com.minhdev.project.service.EmailService;
import com.minhdev.project.util.annotation.ApiMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("email")
    @ApiMessage("Send simple email")
    public String sendSimpleEmail() {
        this.emailService.sendEmailFromTemplate("kirito14042003@gmail.com", "test", "test");
        return "ok";
    }
}
