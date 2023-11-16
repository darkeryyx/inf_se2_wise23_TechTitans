package group.artifact.views;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MainView extends VerticalLayout {

    HorizontalLayout header;
    VerticalLayout content;
    HorizontalLayout fooder;

    MainView(){
       header = new HorizontalLayout(

       );

       content = new VerticalLayout(

       );

       fooder = new HorizontalLayout(

       );
       add(header, content, fooder);
    }
    MainView(VerticalLayout content){
        header = new HorizontalLayout(

        );

        this.content = content;

        fooder = new HorizontalLayout(

        );
        add(header,this.content,fooder);
    }

    void addHeader (HorizontalLayout header){
        this.header = header;
        add(this.header,content,fooder);
    }
    void addContent(VerticalLayout content){
        this.content = content;
        add(header, this.content, fooder);
    }
    void addFooder(HorizontalLayout fooder){
        this.fooder = fooder;
        add(header,content,fooder);
    }

}
