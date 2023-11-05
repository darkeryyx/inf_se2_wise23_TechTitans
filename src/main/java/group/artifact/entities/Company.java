package group.artifact.entities;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "companies", schema = "project")
public class Company {
    // primary keys
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer company_pk;

    // foreign keys
    @OneToOne
    @JoinColumn(name = "user_pk", nullable = false)
    private User user;

    @OneToMany(mappedBy = "company")
    private Set<Offer> offers;

    // attributes
    @Column(nullable = false)
    private String business;
    private Integer employees;
    private LocalDate founded;
    @Column(nullable = false)
    private String link; // to the website of the company
    @Column(nullable = false)
    private String description;
    private String image; // path to logo

    // TODO: correct mapping for inheritence
}
