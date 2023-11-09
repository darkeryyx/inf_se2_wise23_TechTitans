package group.artifact.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import group.artifact.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    // check if first method parameter (?1 = String name) exists, count the
    // occurance and check if its 1
    @Query("SELECT COUNT(u) = 0 FROM User u WHERE u.email = ?1")
    boolean isEmailUnique(String email);

    User findUserByEmail(String email);
}
