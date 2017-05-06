package stud.mi.client;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatClient extends WebSocketClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatClient.class);

    public ChatClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LOGGER.debug("Connection opened");
    }

    @Override
    public void onMessage(String message) {
        LOGGER.trace("Message received: {}", message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LOGGER.debug("Connection closed");
    }

    @Override
    public void onError(Exception ex) {
        LOGGER.error("Error", ex);
    }

}
