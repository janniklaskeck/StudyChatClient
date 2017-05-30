package stud.mi.message;

import com.google.gson.JsonObject;

public class MessageUtil {

	private static final String VERSION = "version";
	private static final String TYPE = "type";
	private static final String USER_ID = "userID";
	private static final String MESSAGE = "message";
	private static final String CONTENT = "content";
	private static final String CHANNEL_NAME = "channelName";
	private static final String USER_NAME = "userName";

	private MessageUtil() {
	}

	public static String buildSendMessage(final String message, final long userID) {
		final JsonObject jo = new JsonObject();
		jo.addProperty(VERSION, 1);
		jo.addProperty(TYPE, MessageType.CHANNEL_MESSAGE);

		final JsonObject joMsg = new JsonObject();
		joMsg.addProperty(USER_ID, userID);
		joMsg.addProperty(MESSAGE, message);

		jo.add(CONTENT, joMsg);
		return jo.toString();
	}

	public static String buildChannelJoinMessage(final String channelName, final long userID) {
		final JsonObject jo = new JsonObject();
		jo.addProperty(VERSION, 1);
		jo.addProperty(TYPE, MessageType.CHANNEL_JOIN);

		final JsonObject joMsg = new JsonObject();
		joMsg.addProperty(USER_ID, userID);
		joMsg.addProperty(CHANNEL_NAME, channelName);

		jo.add(CONTENT, joMsg);
		return jo.toString();
	}

	public static String buildUserJoinMessage(final String userName) {
		final JsonObject jo = new JsonObject();
		jo.addProperty(VERSION, 1);
		jo.addProperty(TYPE, MessageType.USER_JOIN);

		final JsonObject joMsg = new JsonObject();
		joMsg.addProperty(USER_NAME, userName);

		jo.add(CONTENT, joMsg);
		return jo.toString();
	}

}
