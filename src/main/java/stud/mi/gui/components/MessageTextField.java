package stud.mi.gui.components;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import stud.mi.gui.ChatView;
import stud.mi.message.MessageUtil;

public class MessageTextField extends HorizontalLayout {

    private static final long serialVersionUID = -2240425973489197267L;

    private final Button sendButton = new Button("Send Message");
    private final TextField sendTextField = new TextField("Message");

    public MessageTextField() {
        super();
        addComponent(sendTextField);
        addComponent(sendButton);
        sendButton.setClickShortcut(KeyCode.ENTER);
        sendTextField.setWidth("90%");
        setComponentAlignment(sendTextField, Alignment.MIDDLE_CENTER);

        sendButton.setWidth("40%");
        sendButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        setComponentAlignment(sendButton, Alignment.MIDDLE_CENTER);
    }

    public void addClickListener(final ChatView view) {
        sendButton.addClickListener(event -> {
            final String message = sendTextField.getValue();
            view.getClient().send(MessageUtil.buildSendMessage(message, view.getClient().getUserID()));
            sendTextField.clear();
        });
    }

}
