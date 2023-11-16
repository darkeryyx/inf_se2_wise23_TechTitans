package group.artifact.controller;


import group.artifact.dtos.StudentDTO;
import group.artifact.entities.Student;
import group.artifact.entities.User;
import group.artifact.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentController {

    @Autowired
    private StudentService studentService;


    public StudentDTO viewStudentProfile(Integer id) {
        return studentService.viewProfile(id);
    }


    public void createStudentProfile(Student student, User user) {
        studentService.saveProfile(student, user);
    }

    public boolean studentExists(Integer id) {
        return studentService.studentExists(id);
    }
}
