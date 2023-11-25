package group.artifact.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    @JoinColumn(name = "company_fk", nullable = false)
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
}
