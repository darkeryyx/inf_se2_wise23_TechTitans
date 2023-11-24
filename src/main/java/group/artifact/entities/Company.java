package group.artifact.entities;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "companies", schema = "project")
public class Company {
    // primary keys
    @Id
    @Column(name = "user_fk")
    private Integer user_fk;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_fk", nullable = false)
    private User user;

    // foreign keys
    @OneToMany(mappedBy = "company")
    private Set<Offer> offers;

    // attributes
    @NonNull
    @Column(nullable = false)
    private String name;
    @NonNull
    @Column(nullable = false)
    private String business;
    @NonNull
    private Integer employees;
    @NonNull
    private LocalDate founded;
    @NonNull
    @Column(nullable = false)
    private String link; // to the website of the company
    @NonNull
    @Column(nullable = false)
    private String description;
    @NonNull
    private String image; // path to logo
}
