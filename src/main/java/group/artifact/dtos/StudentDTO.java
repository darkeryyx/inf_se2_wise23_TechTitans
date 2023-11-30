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

    void setName(String name);
    void setSurname(String surname);
    void setEmail(String email);
    void setSubject(String subject);
    void setBirthday(LocalDate birthday);
    void setSemester(Integer semester);
    void setSkills(String skills);
    void setInterests(String interests);
    void setDescription(String description);
    void setImage(String image);

}
