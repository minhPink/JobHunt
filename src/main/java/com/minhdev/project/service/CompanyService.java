package com.minhdev.project.service;

import com.minhdev.project.domain.Company;
import com.minhdev.project.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return this.companyRepository.findAll();
    }

    public Company handleUpdateCompany(Company company) {
        Optional<Company> companyOptional = this.companyRepository.findById(company.getId());
        if (companyOptional.isPresent()) {
            Company updatedCompany = companyOptional.get();
            updatedCompany.setName(company.getName());
            updatedCompany.setAddress(company.getAddress());
            updatedCompany.setDescription(company.getDescription());
            updatedCompany.setLogo(company.getLogo());
            return this.companyRepository.save(updatedCompany);
        }
        return null;
    }

    public void handleDeleteCompany(Long id) {
        this.companyRepository.deleteById(id);
    }
}
