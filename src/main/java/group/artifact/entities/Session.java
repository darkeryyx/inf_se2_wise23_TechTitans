package group.artifact.entities;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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