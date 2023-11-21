package group.artifact.dtos.impl;

import java.time.LocalDate;

import group.artifact.dtos.CompanyDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyDTOImpl implements CompanyDTO  {

    private String name;
    private String business;
    private Integer employees;
    private LocalDate founded;
    private String link; // to the website of the company
    private String description;
    private String logo; // path to logo

}
