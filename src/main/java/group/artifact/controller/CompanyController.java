package group.artifact.controller;

import group.artifact.dtos.CompanyDTO;
import group.artifact.entities.Company;
import group.artifact.services.CompanyService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyController {

    @Autowired
    CompanyService companyService;

    public void createCompany(Company company, Integer userId) {
        companyService.saveProfile(company, userId);
    }

    public List<CompanyDTO> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    public boolean companyExists(Integer id) {
        return companyService.companyExists(id);
    }
}
