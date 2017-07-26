package stud.mi.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import stud.mi.message.Message;
import stud.mi.message.MessageType;
import stud.mi.message.MessageUtil;
import stud.mi.util.ChannelMessageListener;
import stud.mi.util.ChatMessageListener;

public class ChatClient extends WebSocketClient
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatClient.class);
    private static final long HEARTBEAT_RATE = 10 * 1000L;
    public static final int PROTOCOL_VERSION = 1;

    private long userID = -1L;
    private String channel = "";
    private ChatMessageListener channelMessageListener;
    private ChannelMessageListener userListener;
    private ChannelMessageListener channelListener;
    private Timer heartBeatTimer;

    public ChatClient(final URI serverURI)
    {
        super(serverURI);
        ChatClient.LOGGER.info("Created ChatClient with Server URI {}.", serverURI);
        this.heartBeatTimer = new Timer(true);
        this.heartBeatTimer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                if (ChatClient.this.isConnected())
                {
                    ChatClient.LOGGER.trace("Running Heartbeat.");
                    ChatClient.this.send(MessageUtil.buildHeartbeatMessage(ChatClient.this.userID).toJson());
                }
            }
        }, 1000L, ChatClient.HEARTBEAT_RATE);
    }

    public void addChannelListener(final ChannelMessageListener listener)
    {
        this.channelListener = listener;
    }

    private void addMessage(final Message msg)
    {
        if (this.channelMessageListener != null)
        {
            this.channelMessageListener.onMessage(msg);
        }
    }

    public void addMessageListener(final ChatMessageListener listener)
    {
        this.channelMessageListener = listener;
    }

    public void addUserListener(final ChannelMessageListener listener)
    {
        this.userListener = listener;
    }

    public void disconnect()
    {
        if (this.isConnected())
        {
            try
            {
                this.closeBlocking();
            }
            catch (InterruptedException e)
            {
                ChatClient.LOGGER.error("Could not close Connection!", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public String getChannel()
    {
        return this.channel;
    }

    public long getUserID()
    {
        return this.userID;
    }

    public boolean isConnected()
    {
        return this.userID != -1L;
    }

    public boolean isConnectedToChannel()
    {
        return this.isConnected() && !this.channel.isEmpty();
    }

    @Override
    public void onClose(final int code, final String reason, final boolean remote)
    {
        ChatClient.LOGGER.debug("Close Connection: Code {}, Reason {}, IsRemote {}", code, reason, remote);
        this.userID = -1L;
        this.channel = "";
        this.userJoinedChannel(new ArrayList<>());
        this.receiveChannelNames(new ArrayList<>());
    }

    @Override
    public void onError(final Exception ex)
    {
        ChatClient.LOGGER.error("Error", ex);
    }

    @Override
    public void onMessage(final String message)
    {
        ChatClient.LOGGER.info("Received Message: {}", message);
        this.parseMessage(message);
    }

    @Override
    public void onOpen(final ServerHandshake handshakedata)
    {
        ChatClient.LOGGER.debug("Open Connection");
    }

    private void parseMessage(final String message)
    {
        final Message msg = new Message(message);
        switch (msg.getType())
        {
        case MessageType.USER_JOIN:
            if (msg.getUserID() > 0)
            {
                this.userID = msg.getUserID();
            }
            break;
        case MessageType.ACK_CHANNEL_JOIN:
            this.channel = msg.getChannelName();
            break;
        case MessageType.CHANNEL_MESSAGE:
            this.addMessage(msg);
            break;
        case MessageType.CHANNEL_USER_CHANGE:
            this.userJoinedChannel(msg.getChannelUserNames());
            break;
        case MessageType.CHANNEL_CHANGE:
            this.receiveChannelNames(msg.getChannelNames());
            break;
        default:
            ChatClient.LOGGER.error("Message Type unknown: {}", msg.getType());
        }
    }

    private void receiveChannelNames(final List<String> channelNames)
    {
        final StringBuilder builder = new StringBuilder();
        for (final String channelName : channelNames)
        {
            builder.append(channelName);
            builder.append(',');
        }
        this.channelListener.onMessage(builder.toString());
    }

    public void stopTimer()
    {
        this.heartBeatTimer.cancel();
    }

    private void userJoinedChannel(final List<String> channelUserNames)
    {
        final StringBuilder builder = new StringBuilder();
        for (final String userName : channelUserNames)
        {
            builder.append(userName);
            builder.append(',');
        }
        this.userListener.onMessage(builder.toString());
    }

}
