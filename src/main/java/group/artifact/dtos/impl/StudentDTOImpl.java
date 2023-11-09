package group.artifact.dtos.impl;

import group.artifact.dtos.StudentDTO;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StudentDTOImpl implements StudentDTO {
    private String name;
    private String surname;
    private String email;
    private String subject; // academic subject
    private LocalDate birthday;
    private Short semester;
    private String skills;
    private String interests;
    private String description;
    private String image;
}
