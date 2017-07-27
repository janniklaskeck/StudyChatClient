package stud.mi.gui.components;

import java.util.Set;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;

import stud.mi.client.ChatClient;
import stud.mi.message.MessageUtil;

public class ChannelList extends GridLayout
{
    private static final long serialVersionUID = -8755547118255795442L;

    private final Label channelListLabel = new Label("Channel List");
    private final ListSelect<String> channelListSelect = new ListSelect<>();
    private final Button joinChannelButton = new Button("Join default Channel");
    private String currentSelectedChannel = "default";

    public ChannelList()
    {
        super(1, 3);
        this.addComponent(this.channelListLabel, 0, 0);
        this.addComponent(this.channelListSelect, 0, 1);
        this.addComponent(this.joinChannelButton, 0, 2);
        this.channelListLabel.setWidth("100%");
        this.channelListSelect.setWidth("100%");
        this.joinChannelButton.setWidth("100%");
        this.setComponentAlignment(this.channelListSelect, Alignment.TOP_LEFT);
        this.setComponentAlignment(this.joinChannelButton, Alignment.TOP_LEFT);
        this.setWidth("100%");
        this.setHeight(ChannelMessageUserComponent.COMPONENT_HEIGHT);
        this.addClickListener();
        this.addSelectListener();
    }

    private void addClickListener()
    {
        this.joinChannelButton.addClickListener(event ->
        {
            if (this.getClient() != null && !this.getClient().isConnectedToChannel())
            {
                final String msg = MessageUtil.buildChannelJoinMessage(this.currentSelectedChannel, this.getClient().getUserID()).toJson();
                this.getClient().send(msg);
            }
        });
    }

    private void addSelectListener()
    {
        this.channelListSelect.addSelectionListener(selectionEvent ->
        {
            this.currentSelectedChannel = selectionEvent.getAllSelectedItems().iterator().next();
            this.joinChannelButton.setCaption(String.format("Join %s Channel", this.currentSelectedChannel));
        });
    }

    public ChatClient getClient()
    {
        final ChannelMessageUserComponent parent = (ChannelMessageUserComponent) this.getParent();
        return parent.getClient();
    }

    public void setChannels(final Set<String> channelNameSet)
    {
        channelNameSet.add("asd");
        channelNameSet.add("asd1");
        channelNameSet.add("asd2");
        this.channelListSelect.setItems(channelNameSet);
    }

}
