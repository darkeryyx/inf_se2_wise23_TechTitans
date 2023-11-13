package group.artifact.services;

import group.artifact.entities.Offer;
import group.artifact.repositories.OfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OfferService {

    private OfferRepository offerRepository;

    public void createOffer(Offer offer) {
        offerRepository.save(offer);
    }
}
