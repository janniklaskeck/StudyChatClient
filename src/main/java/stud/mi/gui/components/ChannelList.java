package stud.mi.gui.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ListSelect;

public class ChannelList extends com.vaadin.ui.GridLayout {

    private static final long serialVersionUID = -8755547118255795442L;

    private final ListSelect<String> channelListSelect = new ListSelect<>();
    private final Button joinChannelButton = new Button("Join Default Channel");

    public ChannelList() {
        super(1, 2);
        addComponent(channelListSelect, 0, 0);
        addComponent(joinChannelButton, 0, 1);
        channelListSelect.setWidth("100%");
        joinChannelButton.setWidth("100%");
        setComponentAlignment(channelListSelect, Alignment.TOP_LEFT);
        setComponentAlignment(joinChannelButton, Alignment.TOP_LEFT);
    }

    public void addClickListener(final ClickListener listener) {
        joinChannelButton.addClickListener(listener);
    }

}
