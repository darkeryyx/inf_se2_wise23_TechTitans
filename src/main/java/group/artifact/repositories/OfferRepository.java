package group.artifact.repositories;

import group.artifact.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Integer> {
    @Query(" SELECT o.description, o.business, o.income, o.job, o.company.name" +
            " FROM Offer o" )
    List<Object[]> findAllOffersAndTheirCompany();

    @Query(" SELECT DISTINCT o.business" +
            " FROM Offer o" )
    List<Object[]> findAllbusinesses();
}
