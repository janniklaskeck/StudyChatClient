package stud.mi.util;

@FunctionalInterface
public interface ChannelMessageListener
{
    void onMessage(final String message);
}
