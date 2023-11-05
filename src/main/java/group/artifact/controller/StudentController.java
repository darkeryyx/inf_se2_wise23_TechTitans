package group.artifact.controller;


import group.artifact.entities.Student;
import group.artifact.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentController {

    @Autowired
    private StudentService studentService;


    public Student viewStudentProfile(Integer id) {
        return studentService.viewProfile(id);
    }


    public void createStudentProfile(Student student, Integer userId) {
        studentService.saveProfile(student, userId);
    }
}
