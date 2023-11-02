package group.artifact.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/*
Unternehmensprofil:
Photo
Unternehmensbeschreibung
Name
Branche
Stellenausschreibungen

Studenten:
Name
Fachrichtung
Skills
*/
@Data
@Entity
@Table(name = "company")
public class Company {

    @Id
    private Integer user_pk;
}
