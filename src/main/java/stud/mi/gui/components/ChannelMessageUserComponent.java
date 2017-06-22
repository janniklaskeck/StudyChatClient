package stud.mi.gui.components;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.RichTextArea;

import stud.mi.gui.ChatView;
import stud.mi.message.MessageUtil;

public class ChannelMessageUserComponent extends GridLayout {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelMessageUserComponent.class);
	private static final long serialVersionUID = 7543517121533849596L;

	private final ChannelList channelList = new ChannelList();
	private final ListSelect<String> userList = new ListSelect<>();
	private final RichTextArea messageTextArea = new RichTextArea();

	private static final String COMPONENT_HEIGHT = "275px";

	public ChannelMessageUserComponent() {
		super(5, 1);

		addComponent(channelList, 0, 0);
		addComponent(messageTextArea, 1, 0, 3, 0);
		addComponent(userList, 4, 0);
		channelList.setWidth("100%");
		channelList.setHeight(COMPONENT_HEIGHT);
		userList.setWidth("100%");
		userList.setHeight(COMPONENT_HEIGHT);
		messageTextArea.setWidth("100%");
		messageTextArea.setHeight(COMPONENT_HEIGHT);
		messageTextArea.setReadOnly(true);
		setWidth("90%");

	}

	public void addClickListener(final ChatView view) {
		channelList.addClickListener(event -> {
			if (!view.getClient().isConnectedToChannel()) {
				view.getClient()
						.send(MessageUtil.buildChannelJoinMessage("default", view.getClient().getUserID()).toJson());
			}
		});
	}

	public void setText(final String value) {
		LOGGER.info("Set Message Area Text");
		messageTextArea.setValue(value);
		messageTextArea.markAsDirty();
	}

	public void setUsers(final String userNames) {
		final Set<String> userNameSet = new HashSet<>();
		final String[] namesSplit = userNames.split(",");
		userNameSet.addAll(Arrays.asList(namesSplit));
		this.userList.setItems(userNameSet);
		userList.markAsDirty();
		LOGGER.info("Set UserList with {} entries", userNameSet.size());
	}

	public void setChannels(final String channelNames) {
		final Set<String> channelNameSet = new HashSet<>();
		final String[] namesSplit = channelNames.split(",");
		channelNameSet.addAll(Arrays.asList(namesSplit));
		this.channelList.setChannels(channelNameSet);
		LOGGER.info("Set ChannelList with {} entries", channelNameSet.size());
	}

}
