package group.artifact.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "offers", schema = "project")
public class Offer {
    // primary keys
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer offer_pk;

    // foreign keys
    @ManyToOne
    @JoinColumn(name = "company_pk", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "offer")
    private Set<Application> applications;

    // attributes
    @NonNull
    private String job;
    @NonNull
    @Column(nullable = false)
    private String business;
    @NonNull
    @Column(nullable = false)
    private String description;
    @NonNull
    private String income;


    public String getLogo() {return company.getLogo();}
    public String getNameOfCompany(){ return company.getName();}
    public String getIncome() {return income;}
}
