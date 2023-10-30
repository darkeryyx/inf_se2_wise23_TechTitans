package group.artifact.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import group.artifact.entities.Session;

public interface SessionRepository extends JpaRepository<Session, Integer>{
    
}
