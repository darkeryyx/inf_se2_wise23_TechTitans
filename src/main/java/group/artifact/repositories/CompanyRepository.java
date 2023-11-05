package group.artifact.repositories;

import group.artifact.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query("SELECT c FROM Company c WHERE c.company_pk = ?1")
    Company getReferenceById(Integer id);
}
