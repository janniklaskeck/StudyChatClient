package stud.mi.gui.components;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import stud.mi.gui.ChatView;
import stud.mi.message.MessageUtil;

public class MessageTextField extends HorizontalLayout
{

    private static final long serialVersionUID = -2240425973489197267L;

    private final Button sendButton = new Button("Send Message");
    private final TextField sendTextField = new TextField();

    public MessageTextField()
    {
        super();
        this.addComponent(this.sendTextField);
        this.addComponent(this.sendButton);
        this.sendButton.setClickShortcut(KeyCode.ENTER);
        this.sendTextField.setWidth("100%");
        this.setExpandRatio(this.sendTextField, 1.0f);
        this.setComponentAlignment(this.sendTextField, Alignment.MIDDLE_RIGHT);

        this.sendButton.setWidth("100%");
        this.sendButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        this.setExpandRatio(this.sendButton, 0.2f);
        this.setComponentAlignment(this.sendButton, Alignment.MIDDLE_LEFT);
        this.setWidth("100%");
        this.addClickListener();
    }

    private void addClickListener()
    {
        final ChatView view = (ChatView) this.getParent();
        this.sendButton.addClickListener(event ->
        {
            if (view.getClient() != null && view.getClient().isConnectedToChannel())
            {
                final String message = this.sendTextField.getValue();
                view.getClient().send(MessageUtil.buildSendMessage(message, view.getClient().getUserID()).toJson());
                this.sendTextField.clear();
            }
        });
    }

}
