package group.artifact.controller;


import group.artifact.entities.Student;
import group.artifact.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class StudentController {

    @Autowired
    private StudentService service;


    public Student viewStudentProfile(Integer id) {
        return service.viewProfile(id);
    }


    public void createStudentProfile(Student student) {
        service.saveProfile(student);
    }
}
