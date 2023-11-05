package group.artifact.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ApplicationCompositeKey implements Serializable{
    @Column(name = "student_pk")
    Integer student_pk;
    @Column(name = "offer_pk")
    Integer offer_pk;
}
