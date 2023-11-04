package group.artifact.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import group.artifact.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    // check if first method parameter (?1 = String name) exists, count the
    // occurance and check if its 1
    @Query("SELECT COUNT(u) = 0 FROM User u WHERE u.email = ?1")
    boolean isEmailUnique(String email);

    // SELECT surname
    // FROM User u
    // WHERE u.surname = [StringValueOf(surname)] AND u.name = [StringValueOf(name)]
    @Query("SELECT u FROM User u WHERE u.surname LIKE ?1 AND u.name LIKE ?2")
    User findUserBySurnameAndName ( String surname , String name);

    // SELECT password
    // FROM User u
    // WHERE u.password LIKE ?1 AND u.name LIKE ?2
    @Query("SELECT u.password FROM User u WHERE u.surname LIKE ?1 AND u.name LIKE ?2")
    String findUserByPassword (String surname, String name);

    // SELECT u.salt
    // FROM user u
    // WHERE u.surname LIKE ?1 AND u.name LIKE ?2
    @Query("SELECT u.salt FROM User u WHERE u.surname LIKE ?1 AND u.name LIKE ?2")
    String findSaltBySurnameAndName(String surname, String name);



}
