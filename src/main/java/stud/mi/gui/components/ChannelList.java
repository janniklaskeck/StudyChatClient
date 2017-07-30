package stud.mi.gui.components;

import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;

import stud.mi.client.ChatClient;
import stud.mi.gui.ChatView;

public class ChannelList extends VerticalLayout
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelList.class);
    private static final long serialVersionUID = -8755547118255795442L;

    private final Label channelListLabel = new Label("Channel List");
    private final ListSelect<String> channelListSelect = new ListSelect<>();
    private final Button joinChannelButton = new Button("Join Lobby");
    private String currentSelectedChannel = "Lobby";

    public ChannelList()
    {
        super();
        this.addComponent(this.channelListLabel);
        this.addComponent(this.channelListSelect);
        this.addComponent(this.joinChannelButton);
        this.channelListLabel.setWidth("100%");
        this.channelListSelect.setWidth("100%");
        this.joinChannelButton.setWidth("100%");

        this.setSpacing(false);
    }

    public void init()
    {
        this.joinChannelButton.addClickListener(event ->
        {
            if (this.getClient() != null)
            {
                this.getClient().changeChannel(this.currentSelectedChannel);
                LOGGER.info("Joining Channel '{}'.", this.currentSelectedChannel);
            }
        });

        this.channelListSelect.addSelectionListener(event ->
        {
            final Optional<String> firstSelectedItem = event.getFirstSelectedItem();
            if (firstSelectedItem.isPresent())
            {
                this.joinChannelButton.setCaption(String.format("Join %s", firstSelectedItem.get()));
                this.currentSelectedChannel = firstSelectedItem.get();
                LOGGER.debug("Select Channel '{}'.", firstSelectedItem.get());
            }
        });

        this.getClient().setOnChannelListUpdateListener(() -> this.setChannels(this.getClient().channelList));
    }

    private ChatClient getClient()
    {
        final ChatView parent = (ChatView) this.getParent();
        return parent.getClient();
    }

    private void setChannels(final Set<String> channelNameSet)
    {
        this.channelListSelect.setItems(channelNameSet);
        this.channelListSelect.markAsDirty();
        LOGGER.debug("Updated Channel List with {} items.", channelNameSet.size());
    }

}
