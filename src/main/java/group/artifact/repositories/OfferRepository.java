package group.artifact.repositories;

import group.artifact.entities.Offer;
import group.artifact.dtos.OfferDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Integer> {

    //TODO: Attribute festlegen
    @Query(" SELECT o.company.image, o.company.name, o.business, o.income, o.job" +
            " FROM Offer o" )
    List<OfferDTO> findAllOffersAndTheirCompany();

    @Query(" SELECT DISTINCT o.business" +
            " FROM Offer o" )
    List<OfferDTO[]> findAllbusinesses();
}
