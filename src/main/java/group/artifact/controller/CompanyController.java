package group.artifact.controller;

import group.artifact.entities.Company;
import group.artifact.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyController {

    @Autowired
    CompanyService companyService;

    public void createCompany(Company company, Integer userId){
        companyService.saveProfile(company, userId);
    }
}
