package stud.mi.gui;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import stud.mi.client.ChatClient;

@Title("Student Chat")
@Theme("valo")
public class Main extends UI {

    private static final long serialVersionUID = -720922173696713289L;

    @Override
    protected void init(VaadinRequest request) {
        initContent();
        URI server = null;
        try {
            server = new URI("ws://studychatserver.mybluemix.net:8080");
        } catch (URISyntaxException e) {
        }
        final ChatClient client = new ChatClient(server);
        client.connect();
    }

    private void initContent() {
        final VerticalLayout verticalLayout = new VerticalLayout();
        final TextArea textArea = new TextArea();
        final TextField textField = new TextField();
        final Button button = new Button("Send Message");
        button.addClickListener(event -> {
            final String message = textField.getValue();
            textArea.setValue(textArea.getValue() + System.lineSeparator() + message);
        });
        verticalLayout.addComponent(textArea);
        verticalLayout.addComponent(textField);
        verticalLayout.addComponent(button);
        setContent(verticalLayout);
    }

    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = Main.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
        private static final long serialVersionUID = -5921182411818280724L;
    }

}