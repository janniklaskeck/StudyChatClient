package stud.mi.message;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Message {

	private static final JsonParser JSON_PARSER = new JsonParser();

	private JsonObject jsonData;

	public Message(final String json) {
		jsonData = JSON_PARSER.parse(json).getAsJsonObject();
	}

	public Message(final JsonObject json) {
		this.jsonData = json;
	}

	public int getVersion() {
		return jsonData.get("version").getAsInt();
	}

	public String getType() {
		return jsonData.get("type").getAsString();
	}

	public Long getUserID() {
		return getContent().get("userID").getAsLong();
	}

	public String getUserName() {
		return getContent().get("userName").getAsString();
	}

	public String getMessage() {
		return getContent().get("message").getAsString();
	}

	public String getChannelName() {
		return getContent().get("channelName").getAsString();
	}

	public List<String> getChannelUserNames() {
		final JsonArray userNamesArray = getContent().get("channelUserNames").getAsJsonArray();
		final List<String> userNames = new ArrayList<>();
		for (final JsonElement userName : userNamesArray) {
			userNames.add(userName.getAsString());
		}
		return userNames;
	}

	public List<String> getChannelNames() {
		final JsonArray channelNamesArray = getContent().get("channelNames").getAsJsonArray();
		final List<String> channelNames = new ArrayList<>();
		for (final JsonElement channelName : channelNamesArray) {
			channelNames.add(channelName.getAsString());
		}
		return channelNames;
	}

	public JsonObject getContent() {
		return jsonData.get("content").getAsJsonObject();
	}

	@Override
	public String toString() {
		return "";
	}

	public String toJson() {
		return jsonData.toString();
	}

}
