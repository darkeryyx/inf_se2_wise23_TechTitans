package group.artifact.services;

import group.artifact.entities.Student;
import group.artifact.entities.User;
import group.artifact.repositories.StudentRepository;
import group.artifact.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class StudentService {

    private StudentRepository studentRepository;
    private UserRepository userRepository;
    private EntityManager entityManager;

    @Transactional
    public void saveProfile(Student student, Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = entityManager.merge(userOptional.get()); // get the user in persistence context
            student.setUser(user); // use the managed instance
            studentRepository.save(student);
        }
    }

    public Student viewProfile(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id " + id));
    }
}
