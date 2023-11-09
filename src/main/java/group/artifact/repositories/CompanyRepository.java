package group.artifact.repositories;

import group.artifact.entities.Company;
import group.artifact.dtos.CompanyDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query("SELECT c.image, c.name, c.link FROM Company c")
    List<CompanyDTO> findAllCompanies();
}
