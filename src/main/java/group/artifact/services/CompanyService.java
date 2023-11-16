package group.artifact.services;

import group.artifact.entities.Company;
import group.artifact.entities.User;
import group.artifact.repositories.CompanyRepository;
import group.artifact.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CompanyService {

    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private EntityManager entityManager;

    @Transactional
    public void saveProfile(Company company, Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = entityManager.merge(userOptional.get()); // get the user in persistence context
            company.setUser(user); // use the managed instance
            companyRepository.save(company);
        }
    }
    public boolean companyExists(Integer id) {
        return companyRepository.existsById(id);
    }
}
