package group.artifact.services;

import group.artifact.dtos.CompanyDTO;
import group.artifact.dtos.CompanyDTO;
import group.artifact.dtos.StudentDTO;
import group.artifact.dtos.impl.CompanyDTOImpl;
import group.artifact.dtos.impl.StudentDTOImpl;
import group.artifact.entities.Company;
import group.artifact.entities.Company;
import group.artifact.entities.Student;
import group.artifact.entities.User;
import group.artifact.repositories.CompanyRepository;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import group.artifact.repositories.UserRepository;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CompanyService {

    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private EntityManager entityManager;
    private ModelMapper mapper;

    @Transactional
    public void saveProfile(Company company, User newUser) {
            User user = entityManager.merge(newUser); // get the user in persistence context
            company.setUser(user); // use the managed instance
            companyRepository.save(company);
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

    public List<String> findAllBusinesses() {return companyRepository.findAllBusinesses();}
    public Optional<Company> findByID(int i) {return companyRepository.findById(i);}

    @Transactional
    public void updateProfile(CompanyDTO companydto, Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User nicht gefunden.");
        }
        User user = userOptional.get();

        Company companyToUpdate;
        Optional<Company> existingCompany = companyRepository.findById(userId);
        if(!existingCompany.isPresent()) {
            throw new RuntimeException("Company nicht gefunden.");
        }
        companyToUpdate = existingCompany.get();
        mapper.map(companydto, companyToUpdate);

        companyToUpdate.setUser(user);
        companyRepository.save(companyToUpdate);
    }

    public CompanyDTO viewProfile(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id " + id));
        return mapToDTO(company);
    }

    private CompanyDTO mapToDTO(Company company) {
        CompanyDTO companyDTO = mapper.map(company, CompanyDTOImpl.class);
        mapper.map(company.getUser(),companyDTO);
        return companyDTO;
    }

    public void updateImage(int id, String i) {
        try {
            Optional<Company> c = companyRepository.findById(id);
            if (c.isEmpty()) {
                throw new NullPointerException();
            }
            Company company = c.get();
            company.setImage(i);
            companyRepository.save(company);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
