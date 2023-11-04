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
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "students", schema = "project")
public class Student {
    // primary keys
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer student_pk;

    // foreign keys
    @OneToOne
    @JoinColumn(name = "user_fk", nullable = false)
    private User user;

    @OneToMany(mappedBy = "student")
    private Set<Application> applications;

       // attributes
    @Column(nullable = false)
    private String subject; // academic subject
    private LocalDate birthday;
    @Column(nullable = false)
    private Short semester;
    private String skills;
    private String interests;
    private String description;
    private String image; // path to image

    public Student(User user, String subject, LocalDate birthday, Short semester, String skills, String interests, String description, String image) {
        this.user = user;
        this.subject = subject;
        this.birthday = birthday;
        this.semester = semester;
        this.skills = skills;
        this.interests = interests;
        this.description = description;
        this.image = image;
    }
}
