package stud.mi.message;

import com.google.gson.JsonObject;

import stud.mi.client.ChatClient;

public class MessageUtil
{

    private static final String VERSION = "version";
    private static final String TYPE = "type";
    private static final String USER_ID = "userID";
    private static final String MESSAGE = "message";
    private static final String CONTENT = "content";
    private static final String CHANNEL_NAME = "channelName";
    private static final String USER_NAME = "userName";
    private static final String CHANNEL_HISTORY = "channelHistory";

    private MessageUtil()
    {
    }

    public static Message buildHeartbeatMessage(final long userID)
    {
        final JsonObject msgBase = buildMessageBaseJson(MessageType.USER_HEARTBEAT);
        final Message msg = new Message(msgBase);
        msg.getContent().addProperty(USER_ID, userID);
        return msg;
    }

    public static Message buildSendMessage(final String message, final long userID)
    {
        final JsonObject msgBase = buildMessageBaseJson(MessageType.CHANNEL_MESSAGE);
        final Message msg = new Message(msgBase);
        msg.getContent().addProperty(USER_ID, userID);
        msg.getContent().addProperty(MESSAGE, message);
        return msg;
    }

    public static Message buildChannelJoinMessage(final String channelName, final long userID)
    {
        final JsonObject msgBase = buildMessageBaseJson(MessageType.CHANNEL_JOIN);
        final Message msg = new Message(msgBase);
        msg.getContent().addProperty(USER_ID, userID);
        msg.getContent().addProperty(CHANNEL_NAME, channelName);
        return msg;
    }

    public static Message buildUserJoinMessage(final String userName)
    {
        final JsonObject msgBase = buildMessageBaseJson(MessageType.USER_JOIN);
        final Message msg = new Message(msgBase);
        msg.getContent().addProperty(USER_NAME, userName);
        return msg;
    }

    public static JsonObject buildMessageBaseJson(final String type)
    {
        final JsonObject jo = new JsonObject();
        jo.addProperty(VERSION, ChatClient.PROTOCOL_VERSION);
        jo.addProperty(TYPE, type);
        jo.add(CONTENT, new JsonObject());
        return jo;
    }

}
