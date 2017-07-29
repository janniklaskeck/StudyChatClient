package stud.mi.gui.components;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;

import stud.mi.client.ChatClient;
import stud.mi.gui.ChatView;

public class UserList extends VerticalLayout
{
    private static final long serialVersionUID = -3374744713057686297L;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserList.class);
    private final Label userListLabel = new Label("User List");
    private final ListSelect<String> userListSelect = new ListSelect<>();

    public UserList()
    {
        super();
        this.addComponent(this.userListLabel);
        this.addComponent(this.userListSelect);
        this.userListLabel.setWidth("100%");
        this.userListSelect.setWidth("100%");
        this.setSpacing(false);
    }

    public void init()
    {
        this.getClient().setOnUserListUpdateListener(() -> this.setUsers(this.getClient().userList));
    }

    private ChatClient getClient()
    {
        final ChatView parent = (ChatView) this.getParent();
        return parent.getClient();
    }

    private void setUsers(final Set<String> userNames)
    {
        this.userListSelect.setItems(userNames);
        UserList.LOGGER.debug("Set UserList with {} entries", userNames.size());
    }

}
