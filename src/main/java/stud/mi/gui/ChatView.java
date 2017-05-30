package stud.mi.gui;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.cdiviewmenu.ViewMenuItem;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.cdi.CDIView;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import stud.mi.client.ChatClient;
import stud.mi.message.MessageUtil;

@CDIView("")
@ViewMenuItem()

public class ChatView extends MVerticalLayout implements View {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChatView.class);

	private static final long serialVersionUID = -5681201225902032837L;

	private static ChatClient client;

	private static final ListSelect<String> userList = new ListSelect<>();
	private static final TextArea messageTextArea = new TextArea();
	private static final TextField textField = new TextField();
	private static final TextField userNameTextField = new TextField();
	private static final Button joinChannelButton = new Button("Join Default Channel");
	private static final Button connectButton = new Button("Connect");
	private static final Button sendButton = new Button("Send Message");
	private static final Label titleLabel = new Label("Student Chat Server");

	private static final String LOCAL_ADDRESS = "ws://127.0.0.1:8080";
	private static final String REMOTE_ADDRESS = "ws://studychatserver.mybluemix.net";

	@PostConstruct
	void init() {
		URI server = null;
		try {
			if (System.getenv("PORT") != null) {
				server = new URI(REMOTE_ADDRESS);
			} else {
				server = new URI(LOCAL_ADDRESS);
			}
		} catch (URISyntaxException e) {
			LOGGER.error("Could create URI.", e);
		}
		client = new ChatClient(server, this);

		connectButton.addClickListener(event -> {
			if ("".equals(userNameTextField.getValue())) {
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

		joinChannelButton
				.addClickListener(event -> client.send(MessageUtil.buildChannelJoinMessage("default", client.userID)));

		sendButton.addClickListener(event -> {
			final String message = textField.getValue();
			client.send(MessageUtil.buildSendMessage(message, client.userID));
			textField.clear();
		});
		sendButton.setClickShortcut(KeyCode.ENTER);
		sendButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		add(titleLabel);
		add(messageTextArea);
		add(joinChannelButton);
		add(textField);
		add(sendButton);
		add(userNameTextField);
		add(connectButton);
		add(userList);

		// button.setStyleName(ValoTheme.BUTTON_LARGE);
		// button.addStyleName(ValoTheme.BUTTON_PRIMARY);

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

	public static void closeConnection() {
		try {
			client.closeBlocking();
		} catch (InterruptedException e) {
			LOGGER.error("Closing of CLient was interrupted.", e);
			Thread.currentThread().interrupt();
		}
	}

}
