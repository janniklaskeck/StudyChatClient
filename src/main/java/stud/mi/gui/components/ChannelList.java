package stud.mi.gui.components;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

import stud.mi.client.ChatClient;

public class ChannelList extends GridLayout
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelList.class);
    private static final long serialVersionUID = -8755547118255795442L;

    private final Label channelListLabel = new Label("Channel List");
    private final ComboBox<String> channelComboBox = new ComboBox<>();
    private final Button joinChannelButton = new Button("Join default Channel");
    private String currentSelectedChannel = "default";

    public ChannelList()
    {
        super(1, 4);
        this.addComponent(this.channelListLabel, 0, 0);
        this.addComponent(this.channelComboBox, 0, 1);
        this.addComponent(this.joinChannelButton, 0, 3);
        this.channelListLabel.setWidth("100%");
        this.channelComboBox.setWidth("100%");
        this.joinChannelButton.setWidth("100%");
        this.setComponentAlignment(this.channelComboBox, Alignment.TOP_LEFT);
        this.setComponentAlignment(this.joinChannelButton, Alignment.TOP_LEFT);
        this.setWidth("100%");
        this.setHeight(ChannelMessageUserComponent.COMPONENT_HEIGHT);
        this.addListeners();
        this.channelComboBox.setEmptySelectionAllowed(false);
    }

    private void addListeners()
    {
        this.joinChannelButton.addClickListener(event ->
        {
            if (this.getClient() != null)
            {
                this.getClient().changeChannel(this.currentSelectedChannel);
                LOGGER.info("Joining Channel '{}'.", this.currentSelectedChannel);
            }
        });
        this.channelComboBox.addSelectionListener(selectionEvent ->
        {
            if (selectionEvent.getSelectedItem().isPresent())
            {
                this.currentSelectedChannel = selectionEvent.getSelectedItem().get();
                this.joinChannelButton.setCaption(String.format("Join %s Channel", this.currentSelectedChannel));
            }
        });
        this.getClient().setOnChannelListUpdateListener(() -> this.setChannels(this.getClient().channelList));
    }

    private ChatClient getClient()
    {
        final ChannelMessageUserComponent parent = (ChannelMessageUserComponent) this.getParent();
        return parent.getClient();
    }

    private void setChannels(final Set<String> channelNameSet)
    {
        this.channelComboBox.setItems(channelNameSet);
        LOGGER.debug("Updated Channel List with {} items.", channelNameSet.size());
    }

}
