package stud.mi.gui;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import stud.mi.client.ChatClient;
import stud.mi.message.MessageUtil;

public class ChatView extends VerticalLayout implements View {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChatView.class);

	private static final long serialVersionUID = -5681201225902032837L;

	private transient ChatClient client;

	private static final ListSelect<String> userList = new ListSelect<>();
	private static final TextArea messageTextArea = new TextArea();
	private static final TextField textField = new TextField();
	private static final TextField userNameTextField = new TextField();
	private static final Button joinChannelButton = new Button("Join Default Channel");
	private static final Button connectButton = new Button("Connect");
	private static final Button sendButton = new Button("Send Message");
	private static final Label titleLabel = new Label("Student Chat Server");

	private static final String LOCAL_ADDRESS = "ws://127.0.0.1:5000";
	private static final String REMOTE_ADDRESS = "ws://studychatserver.mybluemix.net";

	public ChatView() {
		setSizeFull();

		connectButton.addClickListener(event -> {
			if (!"".equals(userNameTextField.getValue())) {
				client = new ChatClient(getServerURI(), this);
				boolean success = false;
				try {
					success = client.connectBlocking();
				} catch (InterruptedException e) {
					LOGGER.error("Connecting was interrupted.", e);
					Thread.currentThread().interrupt();
				}
				if (success) {
					final String userName = userNameTextField.getValue();
					client.send(MessageUtil.buildUserJoinMessage(userName));
				} else {
					LOGGER.error("Connection to Server was unsuccessful!");
				}
			}
		});

		joinChannelButton.addClickListener(event -> {
			if (client.isConnectedToChannel()) {
				client.send(MessageUtil.buildChannelJoinMessage("default", client.getUserID()));
			}
		});

		sendButton.addClickListener(event -> {
			final String message = textField.getValue();
			client.send(MessageUtil.buildSendMessage(message, client.getUserID()));
			textField.clear();
		});
		sendButton.setClickShortcut(KeyCode.ENTER);

		addComponent(titleLabel);
		addComponent(messageTextArea);
		addComponent(joinChannelButton);
		addComponent(textField);
		addComponent(sendButton);
		addComponent(userNameTextField);
		addComponent(connectButton);
		addComponent(userList);

		sendButton.addStyleName(ValoTheme.BUTTON_PRIMARY);

		titleLabel.addStyleName(ValoTheme.LABEL_HUGE);
		titleLabel.addStyleName(ValoTheme.LABEL_H1);

		messageTextArea.setWidth("90%");
		setComponentAlignment(messageTextArea, Alignment.TOP_CENTER);

		setComponentAlignment(joinChannelButton, Alignment.MIDDLE_LEFT);

		textField.setWidth("90%");
		setComponentAlignment(textField, Alignment.MIDDLE_CENTER);

		sendButton.setWidth("90%");
		setComponentAlignment(sendButton, Alignment.MIDDLE_CENTER);

		setMargin(new MarginInfo(false, true, true, true));
		setStyleName(ValoTheme.LAYOUT_CARD);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		LOGGER.info("Entered ChatView.");
	}

	public void addMessage(String userName, String message) {
		final String msg = String.format("%s: %s%s", userName, message, System.lineSeparator());
		final String previous = messageTextArea.getValue();
		messageTextArea.setValue(previous + msg);
	}

	public void closeConnection() {
		try {
			client.closeBlocking();
		} catch (InterruptedException e) {
			LOGGER.error("Closing of CLient was interrupted.", e);
			Thread.currentThread().interrupt();
		}
	}

	private static URI getServerURI() {
		try {
			if (System.getenv("PORT") != null) {
				return new URI(REMOTE_ADDRESS);
			} else {
				return new URI(LOCAL_ADDRESS);
			}
		} catch (URISyntaxException e) {
			LOGGER.error("Could not create URI.", e);
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
