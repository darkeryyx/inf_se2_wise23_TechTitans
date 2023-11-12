package group.artifact.dtos.impl;

import group.artifact.entities.Company;
import group.artifact.dtos.OfferDTO;

public class OfferDTOImpl implements OfferDTO {

    private Company company;
    private String income;

    public String getLogo() {return company.getLogo();}
    public String getNameOfCompany(){ return company.getName();}
    public String getIncome() {return income;}
}
