package com.tinyline.app;

import com.tinyline.tiny2d.TinyRect;
import org.w3c.dom.events.EventTarget;

// Referenced classes of package com.tinyline.app:
//            SVGEvent

public class SVGEventQueue
{

    private SVGEvent queue;

    public SVGEventQueue()
    {
        queue = null;
    }

    public synchronized void reset()
    {
        queue = null;
    }

    public synchronized SVGEvent getNextEvent()
        throws InterruptedException
    {
        while(queue == null) 
        {
            wait();
        }
        SVGEvent svgevent = queue;
        queue = queue.next;
        return svgevent;
    }

    public void handleEvent(SVGEvent svgevent)
    {
        if(svgevent != null)
        {
            EventTarget eventtarget = svgevent.getCurrentTarget();
            if(eventtarget != null)
            {
                eventtarget.dispatchEvent(svgevent);
            }
        }
    }

    public synchronized void postEvent(SVGEvent svgevent)
    {
        if(queue == null)
        {
            queue = svgevent;
        } else
        {
            SVGEvent svgevent1 = queue;
            do
            {
                if(svgevent1.id == svgevent.id && svgevent1.id == 19)
                {
                    ((TinyRect)svgevent1.data).union((TinyRect)svgevent.data);
                    return;
                }
                if(svgevent1.next == null)
                {
                    break;
                }
                svgevent1 = svgevent1.next;
            } while(true);
            svgevent1.next = svgevent;
        }
        notifyAll();
    }

    public synchronized SVGEvent peekEvent()
    {
        return queue;
    }
}
