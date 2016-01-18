/**
 * PlayerListener.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.svgviewer;

import java.net.URL;

import org.apache.log4j.Logger;
import org.w3c.dom.events.Event;

import com.tinyline.svg.AnimationCallback;
import com.tinyline.svg.SVG;
import com.tinyline.svg.SVGAnimationElem;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGGroupElem;
import com.tinyline.svg.SVGImageElem;
import com.tinyline.svg.SVGNode;
import com.tinyline.svg.SVGRect;
import com.tinyline.svg.SVGSVGElem;
import com.tinyline.tiny2d.TinyMatrix;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyPoint;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyUtil;

/**
 * This class represents the events listener for the TinyLine SVG player
 * <p>
 * @author (C) Andrew Girow
 * @version 1.9
 * <p>
 */
public class PlayerListener implements org.w3c.dom.events.EventListener, AnimationCallback
{
	private  static int MAX_ZOOMLEVEL = 5;
	private  static int MIN_ZOOMLEVEL = -5;
	private  int zoomLevel = 0;

	private static Logger logger = (Logger) Logger.getInstance(PlayerListener.class);
	
	private boolean timePaused;
	private long startTime, offsetTime, pauseTime;

	SVGViewer canvas;

	public PlayerListener(SVGViewer canvas)
	{
		this.canvas = canvas;
	}

	/** Returns the current time in seconds relative to
	 * the start time for the current SVG document fragment. */
	public int getCurrentTime()
	{
		long   now =  System.currentTimeMillis()/4;
		return (int)( now - startTime + offsetTime);
	}

	/** Sets a start time for this node. */
	public void setStartTime()
	{
		startTime = System.currentTimeMillis()/4 ;
		offsetTime = 0;
		timePaused = false;
	}

	/**
	 * Adjusts the clock for this SVG document fragment,
	 * establishing a new current time.
	 *
	 * @param seconds  The new current time in seconds relative to
	 * the start time for the current SVG document fragment.
	 */
	public void setCurrentTime(long seconds)
	{
		long   now =   System.currentTimeMillis()/4;
		offsetTime =   seconds - now;
	}

	/**
	 * Suspends (i.e., pauses) all currently running animations
	 * that are defined within the SVG document fragment corresponding
	 * to this 'svg' element, causing the animation clock corresponding
	 * to this document fragment to stand still until it is unpaused.
	 */
	public void pauseAnimations()
	{
		pauseTime = System.currentTimeMillis()/4;
		timePaused = true;
	}

	/**
	 * Unsuspends (i.e., unpauses) currently running animations
	 * that are defined within the SVG document fragment, causing
	 * the animation clock to continue from the time at which it
	 * was suspended.
	 */
	public void resumeAnimations()
	{
		if (timePaused)
		{
			setCurrentTime(pauseTime);
		}
		timePaused = false;
	}

	/** Returns true if this SVG document fragment is in a paused state. */
	public boolean animationsPaused()
	{
		return timePaused;
	}

	/**
	 * <b>SMIL:</b> Posts the SMIL event from an animation element
	 * to an interested party.
	 *
	 * The SMIL event type can be one of
	 * <ul>
	 * <li>
	 *  SVGAnimationElem.BEGIN_EVENT
	 * </li>
	 * <li>
	 *  SVGAnimationElem.END_EVENT
	 * </li>
	 * <li>
	 *  SVGAnimationElem.REPEAT_EVENT
	 * </li>
	 * </ul>
	 *
	 * @param eventType The SMIL event type
	 * @param theEvent The SMIL event
	 */
	public void postSMILEvent(int eventType, TinyString theEvent)
	{
		SVGEvent event;
		switch(eventType)
		{
		case SVGAnimationElem.BEGIN_EVENT:
			event = new SVGEvent(SVGEvent.EVENT_BEGIN, theEvent );
			canvas.postEvent(event);
			break;
		case SVGAnimationElem.END_EVENT:
			event = new SVGEvent(SVGEvent.EVENT_END, theEvent );
			canvas.postEvent(event);
			break;
		case SVGAnimationElem.REPEAT_EVENT:
			event = new SVGEvent(SVGEvent.EVENT_REPEAT, theEvent );
			canvas.postEvent(event);
			break;
		}
	}

	public void doLink(SVGNode linkNode)
	{
		SVGEvent event;
		String url;
		TinyString xlink_href = ((SVGGroupElem)linkNode).xlink_href;
		if(xlink_href == null) return;
		// System.out.println("linkEvent: " + xlink_href);
		// If its a local link
		SVGDocument document =  canvas.raster.document;
		int hashIndex = xlink_href.indexOf('#',0);
		if(hashIndex != -1)
		{
			//local link
			// Go thru all animations. For each anim with id equals
			// the local_link and which begin equals 'indefinite':
			// change the begin onto the current time an fire event.
			TinyString id = xlink_href.substring(hashIndex + 1);
			if(document.resolveLinkBased(id))
			{
				event = new SVGEvent(SVGEvent.EVENT_ANIMATE,null);
				canvas.postEvent(event);
			}
		}
		else
		{
			//external link
			url = new String(xlink_href.data);
//			System.out.println("linkEvent: external link " + url);
			event = new SVGEvent(SVGEvent.EVENT_LOAD,url);
			canvas.postEvent(event);
		}
	}

	/**
	 *  <b>uDOM:</b> Invoked whenever an event occurs of the type for which
	 *  the <tt> EventListener</tt> interface was registered.
	 *  @param evt The <tt>Event</tt> contains contextual information
	 *             about the event.
	 */
	public void handleEvent(Event evt)
	{
//		System.out.println("handleEvent " + evt.getType());

		SVGEvent theEvent = (SVGEvent) evt;
		SVGRect view,origview;
		SVGDocument document;
		TinyPoint point;
		TinyRect  rect;
		SVGEvent event;
		//String url;

		switch(theEvent.id)
		{
		case SVGEvent.EVENT_UPDATE:
			//long t1 = System.currentTimeMillis();
			//System.out.println("UPDATE" + "[ " + data.xmin +"," + data.ymin +","+ data.xmax+"," + data.ymax +"]");
			canvas.raster.setDevClip((TinyRect)theEvent.data);
			canvas.raster.update();
			canvas.raster.sendPixels();
			//logger.debug("Update finalizado");
			

			//long t2 = System.currentTimeMillis();
			//System.out.println("handleUpdateEvent elapsed time ms: " +(t2-t1) );
			break;

		case SVGEvent.EVENT_ANIMATE:
			//    System.out.println("handleAnimateEvent: " + animationsCount );
			document =  canvas.raster.document;
//			System.out.println("document.nActiveAnimations: " + document.nActiveAnimations );
//			if(document.nActiveAnimations > 0)
			{
				rect = document.animate(getCurrentTime());
				event = new SVGEvent(SVGEvent.EVENT_UPDATE, rect );
				canvas.postEvent(event);
			}
			// If there are not finished animations, produce a new
			// animation event.
			if( document.nActiveAnimations > 0 && !animationsPaused() )
			{
				event = new SVGEvent(SVGEvent.EVENT_ANIMATE,null);
				canvas.postEvent(event);
			}
			break;

		case SVGEvent.EVENT_BEGIN:
		case SVGEvent.EVENT_END:
		case SVGEvent.EVENT_REPEAT:
			document =  canvas.raster.document;
			TinyString smilEvent =  (TinyString)theEvent.data;
			if(document.resolveEventBased(smilEvent))
			{
				event = new SVGEvent(SVGEvent.EVENT_ANIMATE,null);
				canvas.postEvent(event);
			}
			break;

		case SVGEvent.EVENT_LOAD:
			// 1. Reset the event queue
			canvas.eventQueue.reset();
			// 2. Load the document
			URL urlSVG = (URL) theEvent.data;
			document = canvas.loadSVG(urlSVG);
			if(document == null)
			{
				break;
			}
			// 3. Set the loaded document to be current document
			//canvas.currentURL = new String(url);
			canvas.raster.setSVGDocument(document);
			// 4. Set the original view
			view     = canvas.raster.view;
			origview = canvas.raster.origview;
			view.x      = origview.x;
			view.y      = origview.y;
			view.width  = origview.width;
			view.height = origview.height;
			// 5. Set the camera transform
			canvas.raster.setCamera();

			// 6. Clear animTargets and add animation elements
			// if are there any
			document.nActiveAnimations = 0;
			document.animTargets.count = 0;
			document.addAnimations(document.root);
			// 7. Set the animation call back for SMIL events
			document.acb = this;
			// 8. Set the start time
			setStartTime();

			// 9. Fire the animation event
			event = new SVGEvent(SVGEvent.EVENT_ANIMATE,null);
			canvas.postEvent(event);

			// 10. Fire the update event
			event = new SVGEvent(SVGEvent.EVENT_UPDATE, canvas.raster.getDevClip() );
			canvas.postEvent(event);
			break;

		case SVGEvent.EVENT_SCROLL:
			document =  canvas.raster.document;
			// If the current document is not zoom and pan anabled then
			// just quit
			if(!document.isZoomAndPanAnable()) return;
			// 1. Get the drag point
			point = (TinyPoint)theEvent.data;
			SVGSVGElem root = (SVGSVGElem)document.root;
			// 2. Get the current scale
			int scale = root.getCurrentScale();
			// 3. Calculate the new current view
			view = canvas.raster.view;
			view.x += TinyUtil.div(point.x<<TinyUtil.FIX_BITS,scale);
			view.y += TinyUtil.div(point.y<<TinyUtil.FIX_BITS,scale);
			// 5. Set the camera transform
			canvas.raster.setCamera();
			// 6. Fire the update event
			event = new SVGEvent(SVGEvent.EVENT_UPDATE, canvas.raster.getDevClip() );
			canvas.postEvent(event);
			break;

		case SVGEvent.EVENT_UNLOAD:
			break;

		case SVGEvent.EVENT_ZOOM:
			document =  canvas.raster.document;
			if(!document.isZoomAndPanAnable()) return;

			TinyNumber direction = (TinyNumber)theEvent.data;
			// zoom in '0' size / 2
			if(direction.val == 0)
			{
				zoomLevel--;
				if(zoomLevel < MIN_ZOOMLEVEL)
				{
					zoomLevel = MIN_ZOOMLEVEL;
					return;
				}
			}
			else //zoom out size * 2
			{
				zoomLevel++;
				if(zoomLevel > MAX_ZOOMLEVEL)
				{
					zoomLevel = MAX_ZOOMLEVEL;
					return;
				}
			}
			SVGRect newView = new SVGRect();
			view = canvas.raster.view;
			int  midX = view.x + view.width/2;
			int  midY = view.y + view.height/2;
			// zoom in '0' size / 2
			if(direction.val == 0)
			{
				newView.width = (view.width/2);
				newView.height = (view.height/2);
			}
			else //zoom out size * 2
			{
				newView.width = (view.width * 2 );
				newView.height = (view.height * 2);
			}
			newView.x = midX - (newView.width) / 2;
			newView.y = midY - (newView.height) / 2;

			view.x = newView.x;
			view.y = newView.y;
			view.width = newView.width;
			view.height = newView.height;
			canvas.raster.setCamera();
			event = new SVGEvent(SVGEvent.EVENT_UPDATE, canvas.raster.getDevClip() );
			canvas.postEvent(event);
			break;

		case SVGEvent.EVENT_PAUSERESUME:
			if(animationsPaused())
			{
				resumeAnimations();
				event = new SVGEvent(SVGEvent.EVENT_ANIMATE,null);
				canvas.postEvent(event);
			}
			else
			{
				pauseAnimations();
			}
			break;

		case SVGEvent.EVENT_QUALITY:
			// 1. Change the antialaisng
			if(canvas.raster.isAntialiased())
			{
				canvas.raster.setAntialiased(false);
			}
			else
			{
				canvas.raster.setAntialiased(true);
			}
			// 2. Set the camera transform
			canvas.raster.setCamera();
			// 3. Fire the update event
			event = new SVGEvent(SVGEvent.EVENT_UPDATE, canvas.raster.getDevClip() );
			canvas.postEvent(event);
			break;

		case SVGEvent.EVENT_ORIGVIEW:
			// 1. Set the original view
			view     = canvas.raster.view;
			origview = canvas.raster.origview;
			view.x      = origview.x;
			view.y      = origview.y;
			view.width  = origview.width;
			view.height = origview.height;
			// 2. Set the camera transform
			canvas.raster.setCamera();
			// 3. Fire the update event
			event = new SVGEvent(SVGEvent.EVENT_UPDATE, canvas.raster.getDevClip() );
			canvas.postEvent(event);
			break;

		case SVGEvent.EVENT_CLICK:
			point = (TinyPoint)theEvent.data;
			System.out.println("Player: linkEvent: (" + point.x +", "+ point.y+") ");
			document =  canvas.raster.document;

			// See if we have any node with link under (x,y)
			SVGNode hitNode = document.root.nodeHitAt(canvas.raster,point);
			if(hitNode == null)
			{
//				System.out.println("linkEvent: hitNode is not found");
				break; // nothing to do
			}
//			System.out.println("linkEvent: hitNode is found" + hitNode);
			SVGNode linkNode = hitNode.seekAElem();
			if(linkNode == null)
			{
//				System.out.println("linkEvent: linkNode is not found");
				break; // nothing to do
			}
//			System.out.println("linkEvent: linkNode is found" + linkNode);
			doLink(linkNode);
			break;

		case SVGEvent.EVENT_ERROR:
			break;

		case SVGEvent.EVENT_FOCUSHIDE:
			document =  canvas.raster.document;
			// If the document does not have link so ...
			if(document.linkTargets.count == 0) return;
			// Otherwise we need to fire focusout for the
			// current link target
			SVGGroupElem node = (SVGGroupElem)document.linkTargets.data[document.linkIndex];
			if(node != null)
			{
				event = new SVGEvent(SVGEvent.EVENT_FOCUSOUT, node);
				canvas.postEvent(event);
			}
			// Remove links from the linkTargets
			document.linkTargets.count = document.linkIndex = 0;
			break;

		case SVGEvent.EVENT_FOCUSIN:
			node = (SVGGroupElem)theEvent.data;
			if( node!= null)
			{
				node.showBounds = true;
				event = new SVGEvent(SVGEvent.EVENT_UPDATE,node.getDevBounds(canvas.raster));
				canvas.postEvent(event);
			}
			break;

		case SVGEvent.EVENT_FOCUSNEXT:
			document =  canvas.raster.document;
			// If the document does not have link so ...
			if(document.linkTargets.count == 0) return;
			if((document.linkIndex + 1)== document.linkTargets.count) return;
			// hide the old link
			node = (SVGGroupElem)document.linkTargets.data[document.linkIndex];
			if(node != null)
			{
				event = new SVGEvent(SVGEvent.EVENT_FOCUSOUT, node);
				canvas.postEvent(event);
			}
			document.linkIndex++;
			// show the new link
			node = (SVGGroupElem)document.linkTargets.data[document.linkIndex];
			if(node != null)
			{
				event = new SVGEvent(SVGEvent.EVENT_FOCUSIN, node);
				canvas.postEvent(event);
			}
			break;

		case SVGEvent.EVENT_FOCUSOUT:
			node = (SVGGroupElem)theEvent.data;
			if( node!= null)
			{
				node.showBounds = false;
				event = new SVGEvent(SVGEvent.EVENT_UPDATE,node.getDevBounds(canvas.raster));
				canvas.postEvent(event);
			}
			break;

		case SVGEvent.EVENT_FOCUSPRESSED:
			document =  canvas.raster.document;
			// If the document does not have link so ...
			if(document.linkTargets.count == 0) return;
			doLink((SVGGroupElem)document.linkTargets.data[document.linkIndex]);
			break;

		case SVGEvent.EVENT_FOCUSPREV:
			document =  canvas.raster.document;
			// If the document does not have link so ...
			if(document.linkTargets.count == 0) return;
			if(document.linkIndex== 0) return;
			// hide the old link
			node = (SVGGroupElem)document.linkTargets.data[document.linkIndex];
			if(node != null)
			{
				event = new SVGEvent(SVGEvent.EVENT_FOCUSOUT, node);
				canvas.postEvent(event);
			}
			document.linkIndex--;
			// show the new link
			node = (SVGGroupElem)document.linkTargets.data[document.linkIndex];
			if(node != null)
			{
				event = new SVGEvent(SVGEvent.EVENT_FOCUSIN, node);
				canvas.postEvent(event);
			}
			break;

		case SVGEvent.EVENT_FOCUSSHOW:
			document =  canvas.raster.document;
			// Add the links to the linkTargets
			document.linkTargets.count = 0;
			document.linkIndex = 0;
			document.addLinks(document.root);
			// If the document does not have links so ...
			if(document.linkTargets.count == 0) return;
			// Show the first link target
			// We need to fire focusin for the current link target
			node = (SVGGroupElem)document.linkTargets.data[document.linkIndex];
			if(node != null)
			{
				event = new SVGEvent(SVGEvent.EVENT_FOCUSIN, node);
				canvas.postEvent(event);
			}
			break;
			
		case SVGEvent.EVENT_LOAD_IMAGE:
			document =  canvas.raster.document;
			SVGSVGElem rootElem = (SVGSVGElem)document.root;
			
			SVGLoadImageEventData evData = (SVGLoadImageEventData) theEvent.data;

			SVGNode selNode = null;
			//Parece que la siguiente linea no es necesaria. La matriz de transformacion es la misma que la del root
			//selNode = rootElem.nodeHitAt(canvas.raster, new TinyPoint(evData.getX(), evData.getY()));
			if (selNode == null) selNode = canvas.raster.root;

			// Calculo de la posicion de la imagen en el SVG
			TinyMatrix inverse = selNode.getGlobalTransform().inverse();
			TinyPoint p = new TinyPoint(evData.getX()<<TinyUtil.FIX_BITS, evData.getY()<<TinyUtil.FIX_BITS);
			inverse.transform(p);
			TinyPoint p2 = new TinyPoint((evData.getX()+evData.getWidth())<<TinyUtil.FIX_BITS, (evData.getY()+evData.getHeight())<<TinyUtil.FIX_BITS);
			inverse.transform(p2);
			// Nuevo elemento imagen
			SVGImageElem imageElem = (SVGImageElem) document.createElement(SVG.ELEM_IMAGE);
			imageElem.x = p.x;
			imageElem.y = p.y;
			imageElem.width = p2.x - p.x;
			imageElem.height = p2.y - p.y;
			imageElem.xlink_href = new TinyString(evData.getPath().toCharArray());

			// Insertar la imagen en el root
			rootElem.addChild(imageElem, rootElem.children.count);
			
			canvas.raster.setDevClip((TinyRect)canvas.raster.getDevClip());
			canvas.raster.update();
			canvas.raster.sendPixels();
			//logger.debug("EVENT_LOAD_IMAGE finalizado");
			break;
		} //switch
	}
}
