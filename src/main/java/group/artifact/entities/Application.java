package group.artifact.entities;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

// connection between student and offer (n:m relation)
@Data
@Entity
@Table(name = "applications", schema = "project")
public class Application {
    // primary keys (composite)
    @EmbeddedId
    private ApplicationCompositeKey application_pk;

    // foreign keys
    @ManyToOne
    @MapsId("student_pk") // in ApplicationCompositeKey
    @JoinColumn(name = "student_pk", nullable = false) // in Student
    private Student student;

    @ManyToOne
    @MapsId("offer_pk") // in ApplicationCompositeKey
    @JoinColumn(name = "offer_pk", nullable = false) // in Offer
    private Offer offer;

    // attributes
    @Column(nullable = false)
    private ZonedDateTime submitted; // timestamp

}
