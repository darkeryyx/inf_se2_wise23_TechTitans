package group.artifact.services;


import group.artifact.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;


}
