package group.artifact.entities;

import java.time.ZonedDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data // constructor, getter, setter
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "users", schema = "project") // mapping to postgres
public class User {
    // primary keys
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // primary key construction
    private Integer user_pk;
    
    // foreign keys
    // mappedBy refers to the property in Session e.g here its User user
    @OneToMany(mappedBy = "user")
    private Set<Session> sessions;

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

}