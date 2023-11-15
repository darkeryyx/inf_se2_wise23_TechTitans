package group.artifact.entities;

import java.time.ZonedDateTime;
import java.util.Set;

import java.util.Map;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.metamodel.mapping.SqlTypedMapping;
import org.hibernate.type.SqlTypes;

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

    @NonNull
    @Column(nullable = false) // Wert der Map
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String,String> sQA;




    // TODO: optional fields
}