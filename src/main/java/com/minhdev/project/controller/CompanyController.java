package com.minhdev.project.controller;

import com.minhdev.project.domain.Company;
import com.minhdev.project.domain.response.ResultPaginationDTO;
import com.minhdev.project.service.CompanyService;
import com.minhdev.project.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @ApiMessage("Create a new company")
    @PostMapping("/companies")
    public ResponseEntity<Company> newCompany(@Valid @RequestBody Company request) {
        Company newCompany = this.companyService.handleCreateCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @ApiMessage("Fetch all company")
    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @Filter Specification<Company> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.getAllCompanies(spec, pageable));
    }

    @ApiMessage("Update company success")
    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.handleUpdateCompany(request));
    }

    @ApiMessage("Delete company success")
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
