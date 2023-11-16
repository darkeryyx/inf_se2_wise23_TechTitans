package group.artifact.services;

import group.artifact.dtos.OfferDTO;
import group.artifact.dtos.impl.OfferDTOImpl;
import group.artifact.entities.Offer;
import group.artifact.repositories.OfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OfferService {

    private OfferRepository offerRepository;

    public void createOffer(Offer offer) {
        offerRepository.save(offer);
    }

    public List<OfferDTO> getAllOffersAndTheirCompany(){
        List<Offer> offers = offerRepository.findAll(); // all offers needs to be parsed to DTO
        ArrayList<OfferDTO> offerDTOS = new ArrayList<>(); // this whill be done by an array list
        for (Offer offer : offers) {
            OfferDTO dto = new OfferDTOImpl();
            dto.setCompany(offer.getCompany());
            dto.setJob(offer.getJob());
            dto.setBusiness(offer.getBusiness());
            dto.setDescription(offer.getDescription());
            dto.setIncome(offer.getIncome());
            offerDTOS.add(dto);
        }
        return offerDTOS;
    }
}
