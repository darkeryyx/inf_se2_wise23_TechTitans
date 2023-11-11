package group.artifact.entities;

import java.time.ZonedDateTime;

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
@Table(name = "sessions", schema = "project")
public class Session {
    // primary keys
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer session_pk;

    // foreign keys
    @ManyToOne
    @JoinColumn(name = "user_fk", nullable = false)
    private User user;

    // attributes
    @Column(nullable = false)
    private String sid; // session id
    private ZonedDateTime login;
}