package group.artifact.dtos;

import java.util.Map;

import group.artifact.entities.User;

public interface UserDTO {
    void setUser(User user);
    void setSQA(Map<String, String> sqa); // security questions and answers

    User getUser();
}
