package stud.mi.gui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

@Theme("valo")
@SpringUI(path = "local")
public class ChatUILocal extends UI {

    private static final long serialVersionUID = 903938514945760669L;

    @Override
    protected void init(VaadinRequest request) {
        setContent(new ChatView(false));
    }
}
