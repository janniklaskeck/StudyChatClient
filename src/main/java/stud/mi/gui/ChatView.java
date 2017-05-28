package stud.mi.gui;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.vaadin.cdiviewmenu.ViewMenuItem;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.cdi.CDIView;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import stud.mi.client.ChatClient;

@CDIView("")
@ViewMenuItem()

public class ChatView extends MVerticalLayout implements View {

    private static final long serialVersionUID = -5681201225902032837L;

    private static ChatClient client;

    private static final ListSelect<String> userList = new ListSelect<>();
    private static final TextArea messageTextArea = new TextArea();
    private static final TextField textField = new TextField();
    private static final TextField userNameTextField = new TextField();
    private static final Button connectButton = new Button("Connect");
    private static final Button button = new Button("Send Message");
    private static final Label label = new Label("Student Chat Server");

    @PostConstruct
    void init() {
        connectButton.addClickListener(event -> {
            if (userNameTextField.getValue() != "") {
                URI server = null;
                try {
                    if (System.getenv("PORT") != null) {
                        server = new URI("ws://studychatserver.mybluemix.net");
                    } else {
                        server = new URI("ws://127.0.0.1:8080");
                    }
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
        add(label);
        add(messageTextArea);
        add(textField);
        add(button);
        add(userNameTextField);
        add(connectButton);
        add(userList);

        Button button = new Button("Fill test data into DB", e -> {
        });
        button.setStyleName(ValoTheme.BUTTON_LARGE);
        button.addStyleName(ValoTheme.BUTTON_PRIMARY);
        add(button);

        setMargin(new MarginInfo(false, true, true, true));
        setStyleName(ValoTheme.LAYOUT_CARD);
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

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

}
