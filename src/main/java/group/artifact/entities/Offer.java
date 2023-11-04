package group.artifact.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "offers", schema = "project")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer offer_pk;

    @ManyToOne
    @JoinColumn(name = "company_pk", nullable = false) // sesstion to user foreign key
    private Company company;

    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String business;
    private String income;
    private String job;
}
