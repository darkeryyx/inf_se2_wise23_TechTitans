package group.artifact.services;

import group.artifact.entities.Company;
import group.artifact.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository repo;

    public void saveProfile(Company company) {
        repo.save(company);
    }
}
