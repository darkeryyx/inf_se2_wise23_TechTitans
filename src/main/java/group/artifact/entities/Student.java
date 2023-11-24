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
