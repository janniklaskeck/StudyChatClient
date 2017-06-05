package stud.mi.gui.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import stud.mi.client.ChatClient;
import stud.mi.gui.ChatView;
import stud.mi.message.MessageUtil;
import stud.mi.util.ClientUtils;

public class LoginTextField extends HorizontalLayout {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginTextField.class);
    private static final long serialVersionUID = -1737631871850305302L;

    private final TextField userNameTextField = new TextField("UserName");
    private final Button connectButton = new Button("Connect");

    public LoginTextField() {
        super();
        addComponent(userNameTextField);
        addComponent(connectButton);
    }

    public void addClickListener(final boolean connectToRemote, final ChatView view) {
        connectButton.addClickListener(event -> {
            if (!"".equals(userNameTextField.getValue())) {
                final ChatClient client = new ChatClient(ClientUtils.getServerURI(connectToRemote));
                view.setClient(client);
                boolean success = false;
                try {
                    success = client.connectBlocking();
                } catch (InterruptedException e) {
                    LOGGER.error("Connecting was interrupted.", e);
                    Thread.currentThread().interrupt();
                }
                if (success) {
                    final String userName = userNameTextField.getValue();
                    client.send(MessageUtil.buildUserJoinMessage(userName).toJson());
                } else {
                    LOGGER.error("Connection to Server was unsuccessful!");
                }
            }
        });
    }

}
