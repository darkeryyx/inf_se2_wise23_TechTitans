package group.artifact.entities;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

// connection between student and offer (n:m relation)
@Data
@Entity
@Table(name = "applications", schema = "project")
public class Application {
    // primary keys
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer application_pk;

    // foreign keys
    @ManyToOne
    @JoinColumn(name = "student_pk", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "offer_pk", nullable = false)
    private Offer offer;

    // attributes
    @Column(nullable = false)
    private ZonedDateTime submitted; // timestamp

}
