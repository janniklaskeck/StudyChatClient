package stud.mi.gui.components;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.GridLayout;

import stud.mi.client.ChatClient;
import stud.mi.gui.ChatView;
import stud.mi.message.Message;

public class ChannelMessageUserComponent extends GridLayout
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelMessageUserComponent.class);
    private static final long serialVersionUID = 7543517121533849596L;

    public static final String COMPONENT_HEIGHT = "275px";
    private final ChannelList channelList = new ChannelList();
    private final UserList userList = new UserList();
    private final ChannelTextArea channelTextArea = new ChannelTextArea();

    public ChannelMessageUserComponent()
    {
        super(5, 1);

        this.addComponent(this.channelList, 0, 0);
        this.addComponent(this.channelTextArea, 1, 0, 3, 0);
        this.addComponent(this.userList, 4, 0);
        this.setWidth("90%");
    }

    public void addListeners()
    {
        final ChatClient client = this.getClient();
        client.addMessageListener(this::addMessage);
        client.addUserListener(this::setUsers);
        client.addChannelListener(this::setChannels);
    }

    public void addMessage(final Message msg)
    {
        this.channelTextArea.addMessage(msg);
    }

    public ChatClient getClient()
    {
        final ChatView view = (ChatView) this.getParent();
        return view.getClient();
    }

    public void setChannels(final String channelNames)
    {
        final Set<String> channelNameSet = new HashSet<>();
        final String[] namesSplit = channelNames.split(",");
        channelNameSet.addAll(Arrays.asList(namesSplit));
        this.channelList.setChannels(channelNameSet);
        ChannelMessageUserComponent.LOGGER.info("Set ChannelList with {} entries", channelNameSet.size());
    }

    public void setUsers(final String userNames)
    {
        this.userList.setUsers(userNames);
    }

}
