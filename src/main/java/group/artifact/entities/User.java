package group.artifact.entities;

import java.time.ZonedDateTime;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data // constructor, getter, setter
@Entity
@RequiredArgsConstructor
@Table(name = "users", schema = "project") // mapping to postgres
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // primary key construction
    private Integer user_pk;

    @NonNull
    @Column(nullable = false)
    private String name;
    @NonNull
    @Column(nullable = false)
    private String surname;
    @NonNull
    @Column(nullable = false)
    private String password;
    private String salt;
    @NonNull
    @Column(nullable = false)
    private String email;
    private ZonedDateTime created;
    private ZonedDateTime last_login;

    // mappedBy refers to the property in Session e.g here its User user
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Session> sessions;
}