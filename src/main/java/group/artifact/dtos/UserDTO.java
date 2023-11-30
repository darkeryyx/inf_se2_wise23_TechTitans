package group.artifact.dtos;

import java.util.Map;

import group.artifact.entities.User;

public interface UserDTO {
    void setUser(User user);
    void setSQA(Map<String, String> sqa); // security questions and answers

    User getUser();

    String getName();
    String getSurname();
    String getEmail();

    void setName(String name);
    void setSurname(String surname);
    void setEmail(String email);
}
