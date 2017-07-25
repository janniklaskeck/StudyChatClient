package stud.mi.util;

import stud.mi.message.Message;

@FunctionalInterface
public interface ChatMessageListener
{

    void onMessage(final Message message);

}
