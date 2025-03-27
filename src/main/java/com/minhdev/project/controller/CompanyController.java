package com.minhdev.project.controller;

import com.minhdev.project.domain.Company;
import com.minhdev.project.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {
    private final CompanyService companyService;
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    @PostMapping("/companies")
    public ResponseEntity<Company> newCompany(@Valid @RequestBody Company request) {
        Company newCompany = this.companyService.handleCreateCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }
}
