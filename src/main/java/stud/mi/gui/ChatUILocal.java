package stud.mi.gui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("valo")
public class ChatUILocal extends UI {

    private static final long serialVersionUID = 903938514945760669L;

    @Override
    protected void init(VaadinRequest request) {
        setContent(new ChatView(false));

    }

    @WebServlet(urlPatterns = "/local", name = "ChatUILocalServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ChatUILocal.class, productionMode = false)
    public static class ChatUILocalServlet extends VaadinServlet {
    }
}
