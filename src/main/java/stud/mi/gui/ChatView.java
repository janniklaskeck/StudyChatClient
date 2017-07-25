package stud.mi.gui;

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

public class ChatView extends GridLayout implements View
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatView.class);

    private static final long serialVersionUID = -5681201225902032837L;

    private transient ChatClient client;

    private final ChannelMessageUserComponent cmuComponent = new ChannelMessageUserComponent();
    private final MessageTextField messageTextField = new MessageTextField();
    private final LoginTextField loginTextField = new LoginTextField();
    private final Label titleLabel = new Label("Student Chat Server");

    public ChatView()
    {
        super(3, 4);
        this.setSizeFull();

        this.setupComponents();

        this.setMargin(new MarginInfo(false, true, true, true));
        this.setStyleName(ValoTheme.LAYOUT_CARD);
    }

    public void closeConnection()
    {
        try
        {
            this.client.closeBlocking();
        }
        catch (InterruptedException e)
        {
            ChatView.LOGGER.error("Closing of CLient was interrupted.", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent)
    {
        ChatView.LOGGER.info("Entered ChatView.");
    }

    public ChatClient getClient()
    {
        return this.client;
    }

    public void setClient(final ChatClient chatClient)
    {
        this.client = chatClient;
        this.client.addMessageListener(this.cmuComponent::addMessage);
        this.client.addUserListener(this.cmuComponent::setUsers);
        this.client.addChannelListener(this.cmuComponent::setChannels);
    }

    private void setupComponents()
    {
        this.addComponent(this.titleLabel, 0, 0, 2, 0);
        this.addComponent(this.cmuComponent, 0, 1, 2, 1);
        this.addComponent(this.messageTextField, 0, 2, 2, 2);
        this.addComponent(this.loginTextField, 0, 3, 1, 3);

        this.titleLabel.addStyleName(ValoTheme.LABEL_HUGE);
        this.titleLabel.addStyleName(ValoTheme.LABEL_H1);
        this.setComponentAlignment(this.titleLabel, Alignment.TOP_CENTER);
    }
}
