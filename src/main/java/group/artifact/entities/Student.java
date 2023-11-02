package group.artifact.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="student", schema="project")
public class Student {

    @Id
    private Integer user_pk;
}
