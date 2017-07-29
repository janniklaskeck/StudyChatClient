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
import stud.mi.gui.components.ChannelList;
import stud.mi.gui.components.ChannelTextArea;
import stud.mi.gui.components.LoginTextField;
import stud.mi.gui.components.MessageTextField;
import stud.mi.gui.components.UserList;
import stud.mi.util.ClientUtils;

public class ChatView extends GridLayout implements View
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatView.class);

    private static final long serialVersionUID = -5681201225902032837L;

    private final Label titleLabel = new Label("Student Chat Server");
    private transient ChatClient client;

    public static final String COMPONENT_HEIGHT = "275px";

    public ChatView()
    {
        super(7, 4);
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
        final MessageTextField messageTextField = new MessageTextField();
        final LoginTextField loginTextField = new LoginTextField();
        final ChannelList channelList = new ChannelList();
        final ChannelTextArea channelTextArea = new ChannelTextArea();
        final UserList userList = new UserList();
        this.addComponent(this.titleLabel, 0, 0, 6, 0);
        this.addComponent(channelList, 0, 1, 1, 1);
        this.addComponent(channelTextArea, 2, 1, 4, 1);
        this.addComponent(userList, 5, 1, 6, 1);
        this.addComponent(messageTextField, 0, 2, 6, 2);
        this.addComponent(loginTextField, 0, 3, 6, 3);

        userList.init();
        channelList.init();
        channelTextArea.init();

        this.titleLabel.addStyleName(ValoTheme.LABEL_HUGE);
        this.titleLabel.addStyleName(ValoTheme.LABEL_H1);
        this.setComponentAlignment(this.titleLabel, Alignment.TOP_CENTER);
        this.setComponentAlignment(channelList, Alignment.TOP_CENTER);
        this.setComponentAlignment(channelTextArea, Alignment.TOP_CENTER);
        this.setComponentAlignment(userList, Alignment.TOP_CENTER);
    }
}
