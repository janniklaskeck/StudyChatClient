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
    private final TextField sendTextField = new TextField();

    public MessageTextField() {
        super();
        addComponent(sendTextField);
        addComponent(sendButton);
        sendButton.setClickShortcut(KeyCode.ENTER);
        sendTextField.setWidth("100%");
        setExpandRatio(sendTextField, 1.0f);
        setComponentAlignment(sendTextField, Alignment.MIDDLE_RIGHT);

        sendButton.setWidth("100%");
        sendButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        setExpandRatio(sendButton, 0.2f);
        setComponentAlignment(sendButton, Alignment.MIDDLE_LEFT);
        setWidth("100%");
    }

    public void addClickListener(final ChatView view) {
        sendButton.addClickListener(event -> {
            final String message = sendTextField.getValue();
            view.getClient().send(MessageUtil.buildSendMessage(message, view.getClient().getUserID()).toJson());
            sendTextField.clear();
        });
    }

}
