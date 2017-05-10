package stud.mi.client;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import stud.mi.gui.MainLayout;

public class ChatClient extends WebSocketClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatClient.class);

    private String userName;

    public ChatClient(URI serverURI, final String userName) {
        super(serverURI);
        this.userName = userName;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LOGGER.debug("Connection opened");
        send("USER " + userName);
    }

    @Override
    public void onMessage(String message) {
        LOGGER.trace("Message received: {}", message);
        MainLayout.addMessage("anon", message, false);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LOGGER.debug("Connection closed: code {}, reason {}, fromRemote {}", code, reason, remote);
    }

    @Override
    public void onError(Exception ex) {
        LOGGER.error("Error", ex);
    }

    @Override
    public void send(final String message) {
        super.send(message);
        LOGGER.debug("Send Message: '{}'", message);
    }

}
