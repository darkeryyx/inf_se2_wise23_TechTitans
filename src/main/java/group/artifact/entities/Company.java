package group.artifact.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "companies", schema = "project")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer company_pk;

    private String business;
    private Integer employees;
    private LocalDate founded;
    private String link; // to the website of the company
    private String description;
    private String logo; // path to logo
    private Offer[] offers; // TODO: create relation

    @OneToOne
    @JoinColumn(name = "user_pk", nullable = false)
    private User user;
}
