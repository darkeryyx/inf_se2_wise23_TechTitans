package group.artifact.dtos.impl;

import group.artifact.dtos.CompanyDTO;

public class CompanyDTOImpl implements CompanyDTO  {

    private String name;
    private String image;
    private String link;

    public String getName() {return name;}
    public String getLink() {return link;}
    public String getLogo() {return image;}
}
