package stud.mi.gui.components;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextArea;

import stud.mi.gui.ChatView;
import stud.mi.message.MessageUtil;

public class ChannelMessageUserComponent extends GridLayout {

    private static final long serialVersionUID = 7543517121533849596L;

    private final ChannelList channelList = new ChannelList();
    private final ListSelect<String> userList = new ListSelect<>();
    private final TextArea messageTextArea = new TextArea();

    public ChannelMessageUserComponent() {
        super(3, 1);

        addComponent(channelList, 0, 0);
        addComponent(messageTextArea, 1, 0);
        addComponent(userList, 2, 0);
        channelList.setWidth("200px");
        userList.setWidth("200px");
        messageTextArea.setWidth("100%");
        messageTextArea.setHeight("300px");
        setWidth("90%");

    }

    public void addClickListener(final ChatView view) {
        channelList.addClickListener(event -> {
            if (!view.getClient().isConnectedToChannel()) {
                view.getClient().send(MessageUtil.buildChannelJoinMessage("default", view.getClient().getUserID()));
            }
        });
    }

    public void setText(final String value) {
        messageTextArea.setValue(value);
    }

}
