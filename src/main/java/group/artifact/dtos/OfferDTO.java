package group.artifact.dtos;

public interface OfferDTO {

    public String getCompanyName();
    public String getJob();
    public String getBusiness();
    public String getDescription();
    public Float getIncomePerHour();

    public void setCompanyName(String company);
    public void setJob(String job);
    public void setBusiness(String business);
    public void setDescription(String description);
    public void setIncomePerHour(Float incomePerHour);
}
