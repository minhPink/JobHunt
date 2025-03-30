package com.minhdev.project.controller;

import com.minhdev.project.domain.Company;
import com.minhdev.project.domain.dto.ResultPaginationDTO;
import com.minhdev.project.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional ) {
        String sCurrentPage = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
        Pageable pageable = PageRequest.of(Integer.parseInt(sCurrentPage) - 1, Integer.parseInt(sPageSize));
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.getAllCompanies(pageable));
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.handleUpdateCompany(request));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
