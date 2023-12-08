package group.artifact.controller;

import group.artifact.dtos.CompanyDTO;
import group.artifact.entities.Company;
import group.artifact.entities.User;
import group.artifact.services.CompanyService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyController {

    @Autowired
    CompanyService companyService;

    public void createCompany(Company company, User user) {
        companyService.saveProfile(company, user);
    }

    public List<CompanyDTO> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    public boolean companyExists(Integer id) {
        return companyService.companyExists(id);
    }
    public List<String> findAllBusinesses() {return companyService.findAllBusinesses();}
    public Optional<Company> findByID(int i) {return companyService.findByID(i);}

    public CompanyDTO viewCompanyProfile(Integer id) {
        return companyService.viewProfile(id);
    }

    public void updateCompanyProfile(CompanyDTO companyDTO, Integer id) {
        companyService.updateProfile(companyDTO, id);
    }

    public void updateImage(int id, String i) {
        companyService.updateImage(id,i);
    }
    public Optional<Company> getByID(int id) {return this.companyService.findByID(id);}
}
