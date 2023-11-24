package group.artifact.entities;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
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
