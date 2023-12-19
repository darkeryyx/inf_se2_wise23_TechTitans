package group.artifact.repositories;

import group.artifact.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {

}
