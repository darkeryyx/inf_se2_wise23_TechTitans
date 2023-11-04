package group.artifact.services;

import group.artifact.entities.Student;
import group.artifact.entities.User;
import group.artifact.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    StudentRepository repo;
    public void saveProfile(Student student) {
        repo.save(student);
    }

    public Student viewProfile(Integer id) {
        return repo.getReferenceById(id);
    }
}
