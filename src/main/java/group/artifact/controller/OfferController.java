package group.artifact.controller;

import group.artifact.dtos.OfferDTO;
import group.artifact.entities.Offer;
import group.artifact.services.OfferService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfferController {

    @Autowired
    private OfferService offerService;

    public void createOffer(Offer offer) {
        offerService.createOffer(offer);
    }

    public List<OfferDTO> getAllOffersAndTheirCompany() {
        return offerService.getAllOffersAndTheirCompany();
    }

    public List<String> getBusinessList() {return offerService.getBusinessList();}

    public String getLogo(int id) {
        return offerService.getLogo(id);
    }
}
