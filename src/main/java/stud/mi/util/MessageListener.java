package stud.mi.util;

@FunctionalInterface
public interface MessageListener {

    void onMessageReceiverListener(final StringBuffer messageBuffer);

}
