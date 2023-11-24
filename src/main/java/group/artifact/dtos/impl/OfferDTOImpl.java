package group.artifact.dtos.impl;

import group.artifact.entities.Company;
import lombok.Data;
import lombok.NoArgsConstructor;
import group.artifact.dtos.OfferDTO;

@Data
@NoArgsConstructor
public class OfferDTOImpl implements OfferDTO {

    private String companyName;
    private String job;
    private String business;
    private String description;
    private String income;
}
