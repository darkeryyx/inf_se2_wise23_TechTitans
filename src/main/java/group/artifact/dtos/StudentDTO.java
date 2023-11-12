package group.artifact.dtos;

import java.time.LocalDate;

public interface StudentDTO {
    String getName();
    String getSurname();
    String getEmail();
    String getSubject();
    LocalDate getBirthday();
    Integer getSemester();
    String getSkills();
    String getInterests();
    String getDescription();
    String getImage();
}
