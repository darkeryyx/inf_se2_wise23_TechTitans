package group.artifact.entities;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data // constructor, getter, setter
@Entity
@Table(name = "user") // mapping to postgres
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // primary key construction
    private int user_pk;
    private String username;
    private String password;
    private String salt;
    private String email;
    private ZonedDateTime created;
    private ZonedDateTime last_login;
}