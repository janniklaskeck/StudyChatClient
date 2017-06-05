package stud.mi.gui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("valo")
public class ChatUI extends UI {

    private static final long serialVersionUID = 903938514945760669L;
    private ChatView view;

    @Override
    protected void init(VaadinRequest request) {
        view = new ChatView(true);
        setContent(view);
        setPollInterval(500);
        addDetachListener(event -> {
            System.out.println("Closing View!");
            if (view.getClient() != null) {
                view.getClient().stopTimer();
            }
        });
    }

    @WebServlet(name = "ChatUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ChatUI.class, productionMode = false)
    public static class ChatUIServlet extends VaadinServlet {
    }
}
