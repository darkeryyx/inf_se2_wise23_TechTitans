package group.artifact.entities;

import java.time.ZonedDateTime;
import java.util.Set;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data // constructor, getter, setter
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
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

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private Student student;

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

    @Column(nullable = false)
    private String question1;
    @Column(nullable = false)
    private String question2;
    @Column(nullable = false)
    private String answer1;
    @Column(nullable = false)
    private String answer2;

    @NonNull
    @Column(nullable = false)
    private Boolean locked;
    /*
     * initial: null
     * after registration: pin with 5 chars
     * when validated: !true (char not boolean)
     */
    private String pin;
}