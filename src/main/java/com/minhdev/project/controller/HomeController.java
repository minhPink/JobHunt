package com.minhdev.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/") // Khi truy cập http://localhost:8080/
    public String home(Model model) {
        model.addAttribute("username", "Minh"); // Gửi dữ liệu đến Thymeleaf
        return "home"; // Trả về home.html trong thư mục templates
    }
}
