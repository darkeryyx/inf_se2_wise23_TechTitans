package group.artifact.controller;

import group.artifact.entities.Company;
import group.artifact.entities.Student;
import group.artifact.services.CompanyService;
import group.artifact.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

    @Autowired
    CompanyService service;

    public void createCompany(Company company){
        service.saveProfile(company);
    }
}
