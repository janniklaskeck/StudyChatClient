package stud.mi.gui.components;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;

import stud.mi.client.ChatClient;

public class UserList extends GridLayout
{
    private static final long serialVersionUID = -3374744713057686297L;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserList.class);
    private final Label userListLabel = new Label("User List");
    private final ListSelect<String> userListSelect = new ListSelect<>();

    public UserList()
    {
        super(1, 2);
        this.addComponent(this.userListLabel, 0, 0);
        this.addComponent(this.userListSelect, 0, 1);
        this.userListLabel.setWidth("100%");
        this.userListSelect.setWidth("100%");
        this.setComponentAlignment(this.userListSelect, Alignment.TOP_LEFT);
        this.setWidth("100%");
        this.setHeight(ChannelMessageUserComponent.COMPONENT_HEIGHT);
        this.addListener();
    }

    private void addListener()
    {
        this.getClient().setOnUserListUpdateListener(() -> this.setUsers(this.getClient().userList));
    }

    private ChatClient getClient()
    {
        final ChannelMessageUserComponent parent = (ChannelMessageUserComponent) this.getParent();
        return parent.getClient();
    }

    private void setUsers(final Set<String> userNames)
    {
        this.userListSelect.setItems(userNames);
        UserList.LOGGER.debug("Set UserList with {} entries", userNames.size());
    }

}
