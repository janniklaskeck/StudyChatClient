package stud.mi.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientUtils.class);

    private static final String LOCAL_ADDRESS = "ws://127.0.0.1:5000";
    private static final String REMOTE_ADDRESS = "ws://studychatserver.mybluemix.net";

    public static URI getServerURI(final boolean isRemote) {
        try {
            if (System.getenv("PORT") != null || isRemote) {
                return new URI(REMOTE_ADDRESS);
            } else {
                return new URI(LOCAL_ADDRESS);
            }
        } catch (URISyntaxException e) {
            LOGGER.error("Could not create URI.", e);
        }
        return null;
    }

}
