package group.artifact.repositories;
import group.artifact.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import group.artifact.entities.PostBox;

import java.util.List;

public interface PostBoxRepository extends JpaRepository<PostBox, Integer> {
    List<PostBox> findByUser(User user);

}
