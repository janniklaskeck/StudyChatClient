package stud.mi.gui.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

import stud.mi.client.ChatClient;
import stud.mi.gui.ChatView;

public class LoginTextField extends HorizontalLayout
{

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginTextField.class);
    private static final long serialVersionUID = -1737631871850305302L;

    private static final String CONNECT_TEXT = "Connect";
    private static final String DISCONNECT_TEXT = "Disconnect";
    private final Label userNameLabel = new Label("UserName");
    private final TextField userNameTextField = new TextField();
    private final Button connectButton = new Button(CONNECT_TEXT);

    public LoginTextField()
    {
        super();
        this.addComponent(this.userNameLabel);
        this.addComponent(this.userNameTextField);
        this.addComponent(this.connectButton);
        this.addClickListener();
    }

    private void addClickListener()
    {
        this.connectButton.addClickListener(event ->
        {
            if (this.getClient().isConnected())
            {
                this.getClient().disconnect();
                this.connectButton.setCaption(CONNECT_TEXT);
                this.userNameTextField.setEnabled(true);
            }
            else
            {
                final String userName = this.userNameTextField.getValue();
                final boolean connectionSuccessful = this.getClient().connectToServer(userName);
                if (connectionSuccessful)
                {
                    this.connectButton.setCaption(DISCONNECT_TEXT);
                    this.userNameTextField.setEnabled(false);
                }
                else
                {
                    LOGGER.error("Connection to Server was unsuccessful!");
                    Notification.show("Could not connect to Server!", Notification.Type.ERROR_MESSAGE);
                }
            }
        });
    }

    private ChatClient getClient()
    {
        final ChatView view = (ChatView) this.getParent();
        return view.getClient();
    }

}
