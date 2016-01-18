/**
 * SVGEventQueue.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
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
