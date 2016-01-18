/**
 * SVGEventQueue.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.app;

import org.w3c.dom.events.EventTarget;

import com.tinyline.tiny2d.TinyRect;

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
