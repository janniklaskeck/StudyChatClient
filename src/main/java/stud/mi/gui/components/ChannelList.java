package stud.mi.gui.components;

import java.util.Set;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;

public class ChannelList extends GridLayout
{
    private static final long serialVersionUID = -8755547118255795442L;

    private final Label channelListLabel = new Label("Channel List");
    private final ListSelect<String> channelListSelect = new ListSelect<>();
    private final Button joinChannelButton = new Button("Join Default Channel");

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
    }

    public void addClickListener(final ClickListener listener)
    {
        this.joinChannelButton.addClickListener(listener);
    }

    public void setChannels(final Set<String> channelNameSet)
    {
        this.channelListSelect.setItems(channelNameSet);
        this.channelListSelect.markAsDirty();
    }

}
