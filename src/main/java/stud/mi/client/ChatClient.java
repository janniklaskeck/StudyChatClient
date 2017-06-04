package stud.mi.client;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import stud.mi.message.Message;
import stud.mi.message.MessageType;
import stud.mi.util.MessageListener;

public class ChatClient extends WebSocketClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatClient.class);

    private long userID = -1L;
    private String channel = "";
    private final StringBuffer messageBuffer = new StringBuffer();
    private MessageListener listener;

    public ChatClient(final URI serverURI) {
        super(serverURI);
        LOGGER.info("Created ChatClient with Server URI {}.", serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LOGGER.debug("Open Connection");
    }

    @Override
    public void onMessage(String message) {
        LOGGER.debug("Received Message: {}", message);
        parseMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LOGGER.debug("Close Connection");

    }

    @Override
    public void onError(Exception ex) {
        LOGGER.error("Error", ex);

    }

    private void parseMessage(final String message) {
        final Message msg = new Message(message);
        switch (msg.getType()) {
        case MessageType.USER_JOIN:
            if (msg.getUserID() > 0) {
                this.userID = msg.getUserID();
            }
            break;
        case MessageType.ACK_CHANNEL_JOIN:
            this.channel = msg.getChannelName();
            break;
        case MessageType.CHANNEL_MESSAGE:
            addMessage(msg);
            break;
        default:
            LOGGER.error("Message Type unknown: {}", msg.getType());
        }
    }

    private void addMessage(final Message msg) {
        final String message = String.format("%s: %s%s", msg.getUserName(), msg.getMessage(), System.lineSeparator());
        this.messageBuffer.append(message);
        if (this.listener != null) {
            listener.onMessageReceiverListener(messageBuffer);
        }
    }

    public void addMessageListener(final MessageListener listener) {
        this.listener = listener;
    }

    public long getUserID() {
        return userID;
    }

    public String getChannel() {
        return channel;
    }

    public boolean isConnected() {
        return userID != -1L;
    }

    public boolean isConnectedToChannel() {
        return isConnected() && !channel.isEmpty();
    }

}
