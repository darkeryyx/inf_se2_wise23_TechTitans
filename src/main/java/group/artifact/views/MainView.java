package group.artifact.views;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MainView extends VerticalLayout {


    MainView(){
        add(header());
        add(new VerticalLayout(
                )

        );
        add(fooder());
    }
    MainView(VerticalLayout content){
        add(header());
        add(content);
        add(fooder());
    }

    HorizontalLayout header (){
        return new HorizontalLayout(

        );
    }

    HorizontalLayout fooder(){
        return new HorizontalLayout(

        );
    }




}
