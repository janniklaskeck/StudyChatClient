package stud.mi.gui;

import org.java_websocket.WebSocket.READYSTATE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import stud.mi.client.ChatClient;
import stud.mi.gui.components.ChannelMessageUserComponent;
import stud.mi.gui.components.LoginTextField;
import stud.mi.gui.components.MessageTextField;
import stud.mi.util.ClientUtils;

public class ChatView extends GridLayout implements View
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatView.class);

    private static final long serialVersionUID = -5681201225902032837L;

    private final Label titleLabel = new Label("Student Chat Server");
    private transient ChatClient client;

    public ChatView()
    {
        super(3, 4);
        this.resetClient();
        this.setSizeFull();

        this.setupComponents();

        this.setMargin(new MarginInfo(false, true, true, true));
        this.setStyleName(ValoTheme.LAYOUT_CARD);
    }

    public void closeConnection()
    {
        this.client.disconnect();
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent)
    {
        LOGGER.info("Entered ChatView.");
    }

    public ChatClient getClient()
    {
        if (this.client == null || this.client.getReadyState() == READYSTATE.CLOSED)
        {
            this.resetClient();
        }
        return this.client;
    }

    private void resetClient()
    {
        this.client = new ChatClient(ClientUtils.getServerURI());
    }

    private void setupComponents()
    {
        final ChannelMessageUserComponent cmuComponent = new ChannelMessageUserComponent();
        final MessageTextField messageTextField = new MessageTextField();
        final LoginTextField loginTextField = new LoginTextField();
        this.addComponent(this.titleLabel, 0, 0, 2, 0);
        this.addComponent(cmuComponent, 0, 1, 2, 1);
        this.addComponent(messageTextField, 0, 2, 2, 2);
        this.addComponent(loginTextField, 0, 3, 1, 3);

        this.titleLabel.addStyleName(ValoTheme.LABEL_HUGE);
        this.titleLabel.addStyleName(ValoTheme.LABEL_H1);
        this.setComponentAlignment(this.titleLabel, Alignment.TOP_CENTER);
    }
}
