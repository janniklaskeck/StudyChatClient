package stud.mi.gui;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import stud.mi.client.ChatClient;

public class MainLayout extends GridLayout {
    private static final long serialVersionUID = -3096627081395780778L;
    private static ChatClient client;

    private static final ListSelect<String> userList = new ListSelect<>();
    private static final TextArea messageTextArea = new TextArea();
    private static final TextField textField = new TextField();
    private static final TextField userNameTextField = new TextField();
    private static final Button connectButton = new Button("Connect");
    private static final Button button = new Button("Send Message");
    private static final Label label = new Label("Student Chat Server");

    public MainLayout(final int columns, final int rows) {
        super(columns, rows);
        setSizeFull();
        messageTextArea.setSizeFull();
        textField.setWidth("100%");
        textField.setHeight("100px");
        userList.setWidth("50%");
        userList.setHeight("100%");
        connectButton.addClickListener(event -> {
            if (userNameTextField.getValue() != "") {
                URI server = null;
                try {
                    server = new URI("ws://studychatserver.mybluemix.net");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                client = new ChatClient(server, userNameTextField.getValue());
                client.connect();
            }
        });
        button.addClickListener(event -> {
            final String message = textField.getValue();
            final String user = userNameTextField.getValue();
            addMessage(user, message, true);
            textField.setValue("");
        });
        button.setClickShortcut(KeyCode.ENTER);
        button.addStyleName(ValoTheme.BUTTON_PRIMARY);
        addComponent(label);
        setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        addComponent(messageTextArea, 0, 1);
        addComponent(textField, 0, 2);
        addComponent(button, 1, 2);
        addComponent(userNameTextField, 0, 3);
        addComponent(connectButton, 1, 3);
        addComponent(userList, 1, 1);
    }

    public static void addMessage(final String user, final String message, final boolean doSend) {
        final Set<String> users = new HashSet<>(userList.getValue());
        if (!users.contains(user)) {
            users.add(user);
            userList.setItems(users);
        }
        String oldValue = messageTextArea.getValue();
        if ("" != oldValue) {
            oldValue += System.lineSeparator();
        }
        messageTextArea.setValue(String.format("%s%s: %s", oldValue, user, message));
        if (doSend) {
            client.send(message);
        }
    }

}
