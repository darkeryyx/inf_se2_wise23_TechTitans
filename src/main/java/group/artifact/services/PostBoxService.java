package group.artifact.services;

import group.artifact.entities.PostBox;
import group.artifact.entities.User;
import group.artifact.repositories.PostBoxRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostBoxService {

    private PostBoxRepository postBoxRepository;


    public PostBox getPostBoxByID(User user) {
       //return postBoxRepository.findById(id);
        List<PostBox> p;
      p=  postBoxRepository.findByUser(user);

      return p.get(0);

    }
}
