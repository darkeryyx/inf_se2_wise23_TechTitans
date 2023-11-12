package group.artifact.services;

import group.artifact.dtos.StudentDTO;
import group.artifact.dtos.impl.StudentDTOImpl;
import group.artifact.entities.Student;
import group.artifact.entities.User;
import group.artifact.repositories.StudentRepository;
import group.artifact.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class StudentService {

    private StudentRepository studentRepository;
    private UserRepository userRepository;
    private EntityManager entityManager;
    private ModelMapper mapper;

    @Transactional
    public void saveProfile(Student student, Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = entityManager.merge(userOptional.get()); // get the user in persistence context
            student.setUser(user); // use the managed instance
            studentRepository.save(student);
        }
    }

    public StudentDTO viewProfile(Integer id) {
         Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id " + id));
         return mapToDTO(student);
    }

    private StudentDTO mapToDTO(Student student) {
        StudentDTO studentDTO = mapper.map(student, StudentDTOImpl.class);
        mapper.map(student.getUser(),studentDTO);
        return studentDTO;
    }
}
