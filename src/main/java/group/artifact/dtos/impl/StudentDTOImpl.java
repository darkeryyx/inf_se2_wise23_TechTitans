package group.artifact.dtos.impl;

import group.artifact.dtos.StudentDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class StudentDTOImpl implements StudentDTO {
    private String name;
    private String surname;
    private String email;
    private String subject; // academic subject
    private LocalDate birthday;
    private Integer semester;
    private String skills;
    private String interests;
    private String description;
    private String image;
}
