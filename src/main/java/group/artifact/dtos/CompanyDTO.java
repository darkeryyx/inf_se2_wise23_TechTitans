package group.artifact.dtos;


import java.time.LocalDate;

public interface CompanyDTO {

    public String getName();
    public String getBusiness();
    public Integer getEmployees();
    public LocalDate getFounded();
    public String getLink(); // to the website of the company
    public String getDescription();
    public String getLogo(); // path to logo

    public void setName(String naString);
    public void setBusiness(String business);
    public void setEmployees(Integer employees);
    public void setFounded(LocalDate founded);
    public void setLink(String link); // to the website of the company
    public void setDescription(String description);
    public void setLogo(String logo); // path to logo
}
