package group.artifact.dtos.impl;

import java.util.Map;

import group.artifact.dtos.UserDTO;
import group.artifact.entities.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class UserDTOImpl implements UserDTO {

    private String name;
    private String surname;
    private String email;

    User user;
    Map<String, String> sqa;

    public UserDTOImpl(User user, Map<String, String> sqa) {
        this.user = user;
        this.sqa = sqa;
        setSQA(sqa);
    }

    @Override
    public void setSQA(Map<String, String> sqa) {
        int i = 0;
        for (Map.Entry<String, String> entry : sqa.entrySet()) {
            if (i == 0) {
                user.setQuestion1(entry.getKey());
                user.setAnswer1(entry.getValue());
                i++;
            } else if (i == 1) {
                user.setQuestion2(entry.getKey());
                user.setAnswer2(entry.getValue());
            }
        }
    }
}
