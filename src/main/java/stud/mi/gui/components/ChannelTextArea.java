package stud.mi.gui.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import stud.mi.client.ChatClient;
import stud.mi.message.Message;

public class ChannelTextArea extends Panel
{

    private static final long serialVersionUID = -744672565761458560L;
    private final List<Label> messages = new ArrayList<>();
    private final VerticalLayout verticalLayout = new VerticalLayout();

    public ChannelTextArea()
    {
        super();
        this.setSizeFull();
        this.setContent(this.verticalLayout);
        this.setWidth("100%");
        this.setHeight(ChannelMessageUserComponent.COMPONENT_HEIGHT);
        this.addListener();
    }

    private void addListener()
    {
        final ChatClient client = this.getClient();
        client.setChatMessageListener(this::addMessage);
    }

    public void addMessage(final Message msg)
    {
        final String message = String.format("<b>%s</b>: %s%s", msg.getUserName(), msg.getMessage(), "<br/>");
        final Label messageLabel = new Label(message);
        messageLabel.setContentMode(ContentMode.HTML);
        messageLabel.setWidth("100%");
        this.messages.add(messageLabel);
        this.verticalLayout.addComponent(messageLabel);
        final int randomOffset = new Random().nextInt(10) - 5;
        this.setScrollTop(Short.MAX_VALUE + randomOffset);
    }

    private ChatClient getClient()
    {
        final ChannelMessageUserComponent parent = (ChannelMessageUserComponent) this.getParent();
        return parent.getClient();
    }
}
