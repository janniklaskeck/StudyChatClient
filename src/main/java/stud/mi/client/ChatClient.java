package stud.mi.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import stud.mi.message.Message;
import stud.mi.message.MessageType;
import stud.mi.message.MessageUtil;
import stud.mi.util.ChannelUpdateListener;
import stud.mi.util.ChatMessageListener;

public class ChatClient extends WebSocketClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatClient.class);
    private static final long HEARTBEAT_RATE = 10 * 1000L;
    public static final int PROTOCOL_VERSION = 1;

    private long userID = -1L;
    private String channel = "";
    private ChatMessageListener chatMessageListener;

    public final Set<String> channelList = new HashSet<>();
    private ChannelUpdateListener onChannelListUpdateListener;

    public final Set<String> userList = new HashSet<>();
    private ChannelUpdateListener onUserListUpdateListener;

    private Timer heartBeatTimer;

    public ChatClient(final URI serverURI)
    {
        super(serverURI);
        LOGGER.info("Created ChatClient with Server URI {}.", serverURI);
        this.setupHeartBeat();
    }

    @Override
    public void onClose(final int code, final String reason, final boolean remote)
    {
        LOGGER.debug("Close Connection: Code {}, Reason {}, IsRemote {}", code, reason, remote);
        this.userID = -1L;
        this.channel = "";
        this.userJoinedChannel(new ArrayList<>());
        this.receiveChannelNames(new ArrayList<>());
    }

    @Override
    public void onError(final Exception ex)
    {
        LOGGER.error("Error", ex);
    }

    @Override
    public void onMessage(final String message)
    {
        LOGGER.info("Received Message: {}", message);
        this.parseMessage(message);
    }

    @Override
    public void onOpen(final ServerHandshake handshakedata)
    {
        LOGGER.debug("Open Connection");
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
            LOGGER.error("Message Type unknown: {}", msg.getType());
        }
    }

    private void receiveChannelNames(final List<String> channelNames)
    {
        this.channelList.clear();
        this.channelList.addAll(channelNames);
        this.onChannelListUpdateListener.onUpdate();
    }

    public void stopTimer()
    {
        this.heartBeatTimer.cancel();
    }

    private void userJoinedChannel(final List<String> channelUserNames)
    {
        this.userList.clear();
        this.userList.addAll(channelUserNames);
        this.onUserListUpdateListener.onUpdate();
    }

    private void addMessage(final Message msg)
    {
        if (this.chatMessageListener != null)
        {
            this.chatMessageListener.onMessage(msg);
        }
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
                LOGGER.error("Could not close Connection!", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public void setChatMessageListener(final ChatMessageListener listener)
    {
        this.chatMessageListener = listener;
    }

    private void setupHeartBeat()
    {
        this.heartBeatTimer = new Timer(true);
        this.heartBeatTimer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                LOGGER.trace("Running Heartbeat.");
                ChatClient.this.send(MessageUtil.buildHeartbeatMessage(ChatClient.this.userID).toJson());
            }
        }, 1000L, HEARTBEAT_RATE);
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

    public void setOnChannelListUpdateListener(final ChannelUpdateListener listener)
    {
        this.onChannelListUpdateListener = listener;
    }

    public void setOnUserListUpdateListener(final ChannelUpdateListener listener)
    {
        this.onUserListUpdateListener = listener;
    }

    @Override
    public void send(final String msg)
    {
        if (this.isConnected())
        {
            super.send(msg);
        }
    }

    public void changeChannel(final String currentSelectedChannel)
    {
        final String msg = MessageUtil.buildChannelJoinMessage(currentSelectedChannel, this.getUserID()).toJson();
        this.send(msg);
    }

    public void sendMessage(final String message)
    {
        if (this.isConnectedToChannel())
        {
            this.send(MessageUtil.buildSendMessage(message, this.getUserID()).toJson());
        }
    }

    public boolean connectToServer(final String userName)
    {
        if (!userName.isEmpty())
        {
            try
            {
                final boolean wasConnectionSuccessful = this.connectBlocking();
                if (wasConnectionSuccessful)
                {
                    this.send(MessageUtil.buildUserJoinMessage(userName).toJson());
                }
                return wasConnectionSuccessful;
            }
            catch (InterruptedException e)
            {
                LOGGER.error("Connecting was interrupted.", e);
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }

}
