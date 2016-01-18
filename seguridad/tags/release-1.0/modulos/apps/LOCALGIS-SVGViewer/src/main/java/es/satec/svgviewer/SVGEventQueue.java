package es.satec.svgviewer;

import org.w3c.dom.events.EventTarget;

import com.tinyline.tiny2d.TinyRect;

/**
 * Clase que encola eventos SVG
 * 
 * @author jpresa
 */
public class SVGEventQueue {

	private SVGEvent queue;

	/**
	 * Creates a new SVGEventQueue object.
	 */
	public SVGEventQueue()
	{
		queue = null;
	}

	/**
	 * Resets the SVGEventQueue.
	 */
	public synchronized void reset()
	{
		queue = null;
	}


	/**
	 * Remove an event from the queue and return it.  This method will
	 * block until an event has been posted by another thread.
	 * @return the next SVGEvent
	 * @exception InterruptedException
	 *            if another thread has interrupted this thread.
	 */
	public synchronized SVGEvent getNextEvent() throws InterruptedException
	{
		while (queue == null)
		{
			wait();
		}
		SVGEvent event = queue;
		queue = queue.next;
		return event;
	}

	/**
	 * Dispatch an event to the eventHandler.
	 *
	 * @param theEvent an instance of SVGEvent, or a
	 * subclass of it.
	 */
	public void handleEvent(SVGEvent theEvent)
	{
		if(theEvent!=null)
		{
			EventTarget target = theEvent.getCurrentTarget();
			if(target!= null)
				target.dispatchEvent(theEvent);
		}
	}


	/**
	 * Post an event to the EventQueue.
	 *
	 * @param theEvent an instance of SVGEvent, or a
	 * subclass of it.
	 */
	public synchronized void postEvent(SVGEvent theEvent)
	{
//		System.out.println("postEvent! 0");
		// empty queue
		if (queue == null)
		{
			queue = theEvent;
		}
		// add to the end
		else
		{
			SVGEvent q = queue;
			for (;;)
			{
				if (q.id == theEvent.id)
				{
					if(q.id == SVGEvent.EVENT_UPDATE)
					{
						((TinyRect)q.data).union((TinyRect)theEvent.data);
						return;
					}
				}
				if (q.next != null)
				{
					q = q.next;
				}
				else
				{
					break;
				}
			} //for(;;)
				q.next = theEvent;
		} //else
			notifyAll();
//		System.out.println("postEvent! 1");
	}

	/**
	 * Return the first event without removing it.
	 */
	public synchronized SVGEvent peekEvent()
	{
		return queue;
	}
}
