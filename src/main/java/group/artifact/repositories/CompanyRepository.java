package group.artifact.repositories;

import group.artifact.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    @Query("Select DISTINCT c.business FROM Company c")
    List<String> findAllBusinesses();
}
