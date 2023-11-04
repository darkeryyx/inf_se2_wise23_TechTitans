package group.artifact.repositories;

import group.artifact.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("SELECT s FROM Student S WHERE s.student_pk = ?1")
    Student getReferenceById(Integer id);

}
