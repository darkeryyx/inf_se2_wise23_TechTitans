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
@Table(name = "students", schema = "project")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer student_pk;

    private String subject; // academic subject
    private LocalDate birthday;
    private Short semester;
    private String skills;
    private String interests;
    private String description;
    private String image; // path to image

    @OneToOne
    @JoinColumn(name = "user_pk", nullable = false)
    private User user;
}
