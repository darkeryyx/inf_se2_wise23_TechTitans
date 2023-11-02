package group.artifact.entities;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;
import lombok.Data;

@Data
@Entity
@Table(name = "sessions", schema="project")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer session_pk;
    @ManyToOne
    @JoinColumn(name = "user_pk", foreignKey = @ForeignKey(name = "session_user_fkey")) // sesstion to user foreign key
    private User user;
    private String sid; // session id
    private ZonedDateTime login;
}