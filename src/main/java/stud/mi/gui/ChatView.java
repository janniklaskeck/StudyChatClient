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

public class ChatView extends GridLayout implements View {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatView.class);

    private static final long serialVersionUID = -5681201225902032837L;

    private transient ChatClient client;

    private final ChannelMessageUserComponent cmuComponent = new ChannelMessageUserComponent();
    private final MessageTextField messageTextField = new MessageTextField();
    private final LoginTextField loginTextField = new LoginTextField();
    private final Label titleLabel = new Label("Student Chat Server");

    private boolean connectToRemote = false;

    public ChatView(final boolean isRemote) {
        super(3, 4);
        connectToRemote = isRemote;
        setSizeFull();

        setupComponents();
        setupActions();

        setMargin(new MarginInfo(false, true, true, true));
        setStyleName(ValoTheme.LAYOUT_CARD);
    }

    private void setupActions() {
        loginTextField.addClickListener(connectToRemote, this);
        cmuComponent.addClickListener(this);
        messageTextField.addClickListener(this);
    }

    private void setupComponents() {
        addComponent(titleLabel, 0, 0, 2, 0);
        addComponent(cmuComponent, 0, 1, 2, 1);
        addComponent(messageTextField, 0, 2, 2, 2);
        addComponent(loginTextField, 0, 3, 1, 3);

        titleLabel.addStyleName(ValoTheme.LABEL_HUGE);
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        setComponentAlignment(titleLabel, Alignment.TOP_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        LOGGER.info("Entered ChatView.");
    }

    public void closeConnection() {
        try {
            client.closeBlocking();
        } catch (InterruptedException e) {
            LOGGER.error("Closing of CLient was interrupted.", e);
            Thread.currentThread().interrupt();
        }
    }

    public void setClient(final ChatClient chatClient) {
        this.client = chatClient;
        this.client.addMessageListener(cmuComponent::setText);
        this.client.addUserListener(cmuComponent::setUsers);
    }

    public ChatClient getClient() {
        return this.client;
    }
}
