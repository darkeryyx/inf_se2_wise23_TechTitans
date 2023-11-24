package group.artifact.services;

import group.artifact.dtos.CompanyDTO;
import group.artifact.dtos.impl.CompanyDTOImpl;
import group.artifact.entities.Company;
import group.artifact.entities.User;
import group.artifact.repositories.CompanyRepository;
import group.artifact.repositories.UserRepository;
import javax.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
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

    public List<CompanyDTO> getAllCompanies() {
        List<Company> companies = companyRepository.findAll(); // all companies needs to be parsed to DTO
        ArrayList<CompanyDTO> companyDTOS = new ArrayList<>(); // this whill be done by an array list
        for (Company company : companies) {
            CompanyDTO dto = new CompanyDTOImpl();
            dto.setName(company.getName());
            dto.setBusiness(company.getBusiness());
            dto.setEmployees(company.getEmployees());
            dto.setFounded(company.getFounded());
            dto.setLink(company.getLink());
            dto.setDescription(company.getDescription());
            dto.setLogo(company.getImage());
            companyDTOS.add(dto);
        }
        return companyDTOS;
    }

    public boolean companyExists(Integer id) {
        return companyRepository.existsById(id);
    }
}
