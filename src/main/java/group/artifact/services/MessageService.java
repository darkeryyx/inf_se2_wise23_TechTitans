package group.artifact.services;


import group.artifact.controller.PostBoxController;
import group.artifact.entities.Message;
import group.artifact.entities.PostBox;
import group.artifact.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PostBoxController boxController;

    public void createMessage(Message message) {
        messageRepository.save(message);
    }

   /* public void sendMessage(Message message) {
        boxController.addMessage(message);

    }*/

    public Message showMessage(PostBox postBoxID){
        //todo: später find all
      return  messageRepository.findById(postBoxID.getPostBoxID()).get();
    }


}
