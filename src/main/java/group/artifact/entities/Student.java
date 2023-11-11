package group.artifact.entities;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students", schema = "project")
public class Student {
    // primary keys
    @Id
    @Column(name = "user_fk")
    private Integer user_fk;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_fk", nullable = false)
    private User user;

    // foreign keys
    @OneToMany(mappedBy = "student")
    private Set<Application> applications;

    // attribute
    @Column(nullable = false)
    private String subject; // academic subject

    private LocalDate birthday;

    @Column(nullable = false)
    private Integer semester;

    private String skills;

    private String interests;

    private String description;

    private String image; // path to image
}
