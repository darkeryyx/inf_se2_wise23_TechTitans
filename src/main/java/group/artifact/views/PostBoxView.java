package group.artifact.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import group.artifact.controller.MessageController;
import group.artifact.controller.PostBoxController;
import group.artifact.controller.UserController;
import group.artifact.entities.Message;
import group.artifact.entities.PostBox;
import group.artifact.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Optional;

@Route("postbox")
@AnonymousAllowed
public class PostBoxView extends Composite<Component>{
    @Autowired
    private PostBoxController postBoxController;
    @Autowired
    private UserController userController;
    @Autowired
    private MessageController messageController;


    protected Component initContent(){
        User user= userController.getCurrentUser();
        VerticalLayout testLayout = new VerticalLayout();
        //PostBox postbox=postBoxController.getPostBoxByID(user.getUser_pk());
        //Message m=messageController.showMessage(postbox);
        TextField text= new TextField("User-ID");
        testLayout.add(text);
       // testLayout.add(m.getContent());

        return testLayout;


    }


}
