package group.artifact.services;

import group.artifact.dtos.StudentDTO;
import group.artifact.dtos.impl.StudentDTOImpl;
import group.artifact.entities.Student;
import group.artifact.entities.User;
import group.artifact.repositories.StudentRepository;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class StudentService {

    private StudentRepository studentRepository;
    private EntityManager entityManager;
    private ModelMapper mapper;

    @Transactional
    public void saveProfile(Student student, User user) {
        student.setUser(entityManager.merge(user)); // use the managed instance
        studentRepository.save(student);
    }

    public StudentDTO viewProfile(Integer id) {
         Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id " + id));
         return mapToDTO(student);
    }

    public boolean studentExists(Integer id) {
        return studentRepository.existsById(id);
    }

    private StudentDTO mapToDTO(Student student) {
        StudentDTO studentDTO = mapper.map(student, StudentDTOImpl.class);
        mapper.map(student.getUser(),studentDTO);
        return studentDTO;
    }
}
