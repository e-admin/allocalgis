package org.w3c.dom.events;


// Referenced classes of package org.w3c.dom.events:
//            EventListener, Event

public interface EventTarget
{

    public abstract void addEventListener(String s, EventListener eventlistener, boolean flag);

    public abstract void removeEventListener(String s, EventListener eventlistener, boolean flag);

    public abstract boolean dispatchEvent(Event event);
}
