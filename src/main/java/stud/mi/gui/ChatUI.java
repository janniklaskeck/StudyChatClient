package stud.mi.gui;

import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("valo")
public class ChatUI extends UI
{

    @WebServlet(name = "ChatUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ChatUI.class, productionMode = false)
    public static class ChatUIServlet extends VaadinServlet
    {
        private static final long serialVersionUID = -6216866496615055637L;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatUI.class);

    private static final long serialVersionUID = 903938514945760669L;

    @Override
    protected void init(final VaadinRequest request)
    {
        final ChatView chatView = new ChatView();

        this.setContent(chatView);
        this.setPollInterval(500);
        this.addDetachListener(event ->
        {
            ChatUI.LOGGER.info("Closing View!");
            if (chatView.getClient() != null)
            {
                chatView.getClient().stopTimer();
            }
        });
    }
}
