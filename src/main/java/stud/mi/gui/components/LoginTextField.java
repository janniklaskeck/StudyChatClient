package stud.mi.gui.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import stud.mi.gui.ChatView;
import stud.mi.message.MessageUtil;

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
        final ChatView view = (ChatView) this.getParent();
        this.connectButton.addClickListener(event ->
        {
            if (view.getClient().isConnected())
            {
                view.getClient().disconnect();
                this.connectButton.setCaption(CONNECT_TEXT);
                this.userNameTextField.setEnabled(true);
            }
            else
            {
                final boolean connectionSuccessful = this.connectToServer(view);
                if (connectionSuccessful)
                {
                    final String userName = this.userNameTextField.getValue();
                    view.getClient().send(MessageUtil.buildUserJoinMessage(userName).toJson());
                    this.connectButton.setCaption(DISCONNECT_TEXT);
                    this.userNameTextField.setEnabled(false);
                }
                else
                {
                    LOGGER.error("Connection to Server was unsuccessful!");
                }

            }
        });
    }

    private boolean connectToServer(final ChatView view)
    {
        if (!"".equals(this.userNameTextField.getValue()))
        {
            try
            {
                return view.getClient().connectBlocking();
            }
            catch (InterruptedException e)
            {
                LOGGER.error("Connecting was interrupted.", e);
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }

}
