/**
 * PlayerListener.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.app;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

import com.tinyline.svg.AnimationCallback;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGGroupElem;
import com.tinyline.svg.SVGNode;
import com.tinyline.svg.SVGRect;
import com.tinyline.svg.SVGSVGElem;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyPoint;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyUtil;

// Referenced classes of package com.tinyline.app:
//            SVGEvent, PPSVGCanvas, SVGEventQueue

public class PlayerListener
    implements EventListener, AnimationCallback
{

    private static int MAX_ZOOMLEVEL = 5;
    private static int MIN_ZOOMLEVEL = -5;
    private int zoomLevel;
    private boolean timePaused;
    private long startTime;
    private long offsetTime;
    private long pauseTime;
    PPSVGCanvas canvas;

    public PlayerListener(PPSVGCanvas ppsvgcanvas)
    {
        zoomLevel = 0;
        canvas = ppsvgcanvas;
    }

    public int getCurrentTime()
    {
        long l = System.currentTimeMillis() / (long)4;
        return (int)((l - startTime) + offsetTime);
    }

    public void setStartTime()
    {
        startTime = System.currentTimeMillis() / (long)4;
        offsetTime = 0L;
        timePaused = false;
    }

    public void setCurrentTime(long l)
    {
        long l1 = System.currentTimeMillis() / (long)4;
        offsetTime = l - l1;
    }

    public void pauseAnimations()
    {
        pauseTime = System.currentTimeMillis() / (long)4;
        timePaused = true;
    }

    public void resumeAnimations()
    {
        if(timePaused)
        {
            setCurrentTime(pauseTime);
        }
        timePaused = false;
    }

    public boolean animationsPaused()
    {
        return timePaused;
    }

    public void postSMILEvent(int i, TinyString tinystring)
    {
        switch(i)
        {
        case 0: // '\0'
            SVGEvent svgevent = new SVGEvent(1, tinystring);
            canvas.postEvent(svgevent);
            break;

        case 1: // '\001'
            SVGEvent svgevent1 = new SVGEvent(3, tinystring);
            canvas.postEvent(svgevent1);
            break;

        case 2: // '\002'
            SVGEvent svgevent2 = new SVGEvent(16, tinystring);
            canvas.postEvent(svgevent2);
            break;
        }
    }

    public void doLink(SVGNode svgnode)
    {
        TinyString tinystring = ((SVGGroupElem)svgnode).xlink_href;
        if(tinystring == null)
        {
            return;
        }
        SVGDocument svgdocument = canvas.raster.document;
        int i = tinystring.indexOf(35, 0);
        if(i != -1)
        {
            TinyString tinystring1 = tinystring.substring(i + 1);
            if(svgdocument.resolveLinkBased(tinystring1))
            {
                SVGEvent svgevent = new SVGEvent(0, null);
                canvas.postEvent(svgevent);
            }
        } else
        {
            String s = new String(tinystring.data);
            SVGEvent svgevent1 = new SVGEvent(12, s);
            canvas.postEvent(svgevent1);
        }
    }

    public void handleEvent(Event event)
    {
        SVGEvent svgevent = (SVGEvent)event;
        switch(svgevent.id)
        {
        case 4: // '\004'
        case 18: // '\022'
        default:
            break;

        case 19: // '\023'
            canvas.raster.setDevClip((TinyRect)svgevent.data);
            canvas.raster.update();
            canvas.raster.sendPixels();
            break;

        case 0: // '\0'
            SVGDocument svgdocument = canvas.raster.document;
            TinyRect tinyrect = svgdocument.animate(getCurrentTime());
            SVGEvent svgevent1 = new SVGEvent(19, tinyrect);
            canvas.postEvent(svgevent1);
            if(svgdocument.nActiveAnimations > 0 && !animationsPaused())
            {
                SVGEvent svgevent2 = new SVGEvent(0, null);
                canvas.postEvent(svgevent2);
            }
            break;

        case 1: // '\001'
        case 3: // '\003'
        case 16: // '\020'
            SVGDocument svgdocument1 = canvas.raster.document;
            TinyString tinystring = (TinyString)svgevent.data;
            if(svgdocument1.resolveEventBased(tinystring))
            {
                SVGEvent svgevent3 = new SVGEvent(0, null);
                canvas.postEvent(svgevent3);
            }
            break;

        case 12: // '\cargar fichero'
            canvas.eventQueue.reset();
            String s = (String)svgevent.data;
            SVGDocument svgdocument2 = canvas.loadSVG(s);
            if(svgdocument2 != null)
            {
                canvas.currentURL = new String(s);
                canvas.raster.setSVGDocument(svgdocument2);
                SVGRect svgrect = canvas.raster.view;
                SVGRect svgrect4 = canvas.raster.origview;
                svgrect.x = svgrect4.x;
                svgrect.y = svgrect4.y;
                svgrect.width = svgrect4.width;
                svgrect.height = svgrect4.height;
                canvas.raster.setCamera();
                svgdocument2.nActiveAnimations = 0;
                svgdocument2.animTargets.count = 0;
                svgdocument2.addAnimations(svgdocument2.root);
                svgdocument2.acb = this;
                setStartTime();
                SVGEvent svgevent4 = new SVGEvent(0, null);
                canvas.postEvent(svgevent4);
                svgevent4 = new SVGEvent(19, canvas.raster.getDevClip());
                canvas.postEvent(svgevent4);
            }
            break;

        case 17: // '\021'
            SVGDocument svgdocument3 = canvas.raster.document;
            if(!svgdocument3.isZoomAndPanAnable())
            {
                return;
            }
            TinyPoint tinypoint = (TinyPoint)svgevent.data;
            SVGSVGElem svgsvgelem = (SVGSVGElem)svgdocument3.root;
            int i = svgsvgelem.getCurrentScale();
            SVGRect svgrect1 = canvas.raster.view;
            svgrect1.x += TinyUtil.div(tinypoint.x << 8, i);
            svgrect1.y += TinyUtil.div(tinypoint.y << 8, i);
            canvas.raster.setCamera();
            SVGEvent svgevent5 = new SVGEvent(19, canvas.raster.getDevClip());
            canvas.postEvent(svgevent5);
            break;

        case 20: // '\024'
            SVGDocument svgdocument4 = canvas.raster.document;
            if(!svgdocument4.isZoomAndPanAnable())
            {
                return;
            }
            TinyNumber tinynumber = (TinyNumber)svgevent.data;
            if(tinynumber.val == 0)
            {
                zoomLevel--;
                if(zoomLevel < MIN_ZOOMLEVEL)
                {
                    zoomLevel = MIN_ZOOMLEVEL;
                    return;
                }
            } else
            {
                zoomLevel++;
                if(zoomLevel > MAX_ZOOMLEVEL)
                {
                    zoomLevel = MAX_ZOOMLEVEL;
                    return;
                }
            }
            SVGRect svgrect6 = new SVGRect();
            SVGRect svgrect2 = canvas.raster.view;
            int j = svgrect2.x + svgrect2.width / 2;
            int k = svgrect2.y + svgrect2.height / 2;
            if(tinynumber.val == 0)
            {
                svgrect6.width = svgrect2.width / 2;
                svgrect6.height = svgrect2.height / 2;
            } else
            {
                svgrect6.width = svgrect2.width * 2;
                svgrect6.height = svgrect2.height * 2;
            }
            svgrect6.x = j - svgrect6.width / 2;
            svgrect6.y = k - svgrect6.height / 2;
            svgrect2.x = svgrect6.x;
            svgrect2.y = svgrect6.y;
            svgrect2.width = svgrect6.width;
            svgrect2.height = svgrect6.height;
            canvas.raster.setCamera();
            SVGEvent svgevent6 = new SVGEvent(19, canvas.raster.getDevClip());
            canvas.postEvent(svgevent6);
            break;

        case 14: // '\016'
            if(animationsPaused())
            {
                resumeAnimations();
                SVGEvent svgevent7 = new SVGEvent(0, null);
                canvas.postEvent(svgevent7);
            } else
            {
                pauseAnimations();
            }
            break;

        case 15: // '\017'
            if(canvas.raster.isAntialiased())
            {
                canvas.raster.setAntialiased(false);
            } else
            {
                canvas.raster.setAntialiased(true);
            }
            canvas.raster.setCamera();
            SVGEvent svgevent8 = new SVGEvent(19, canvas.raster.getDevClip());
            canvas.postEvent(svgevent8);
            break;

        case 13: // '\r'
            SVGRect svgrect3 = canvas.raster.view;
            SVGRect svgrect5 = canvas.raster.origview;
            svgrect3.x = svgrect5.x;
            svgrect3.y = svgrect5.y;
            svgrect3.width = svgrect5.width;
            svgrect3.height = svgrect5.height;
            canvas.raster.setCamera();
            SVGEvent svgevent9 = new SVGEvent(19, canvas.raster.getDevClip());
            canvas.postEvent(svgevent9);
            break;

        case 2: // '\002'
            TinyPoint tinypoint1 = (TinyPoint)svgevent.data;
            System.out.println("Player: linkEvent: (" + tinypoint1.x + ", " + tinypoint1.y + ") ");
            SVGDocument svgdocument5 = canvas.raster.document;
            SVGNode svgnode = svgdocument5.root.nodeHitAt(canvas.raster, tinypoint1);
            if(svgnode == null)
            {
                break;
            }
            SVGNode svgnode1 = svgnode.seekAElem();
            if(svgnode1 != null)
            {
                doLink(svgnode1);
            }
            break;

        case 5: // '\005'
            SVGDocument svgdocument6 = canvas.raster.document;
            if(svgdocument6.linkTargets.count == 0)
            {
                return;
            }
            SVGGroupElem svggroupelem = (SVGGroupElem)svgdocument6.linkTargets.data[svgdocument6.linkIndex];
            if(svggroupelem != null)
            {
                SVGEvent svgevent10 = new SVGEvent(8, svggroupelem);
                canvas.postEvent(svgevent10);
            }
            svgdocument6.linkTargets.count = svgdocument6.linkIndex = 0;
            break;

        case 6: // '\006'
            SVGGroupElem svggroupelem1 = (SVGGroupElem)svgevent.data;
            if(svggroupelem1 != null)
            {
                svggroupelem1.showBounds = true;
                SVGEvent svgevent11 = new SVGEvent(19, svggroupelem1.getDevBounds(canvas.raster));
                canvas.postEvent(svgevent11);
            }
            break;

        case 7: // '\007'
            SVGDocument svgdocument7 = canvas.raster.document;
            if(svgdocument7.linkTargets.count == 0)
            {
                return;
            }
            if(svgdocument7.linkIndex + 1 == svgdocument7.linkTargets.count)
            {
                return;
            }
            SVGGroupElem svggroupelem2 = (SVGGroupElem)svgdocument7.linkTargets.data[svgdocument7.linkIndex];
            if(svggroupelem2 != null)
            {
                SVGEvent svgevent12 = new SVGEvent(8, svggroupelem2);
                canvas.postEvent(svgevent12);
            }
            svgdocument7.linkIndex++;
            svggroupelem2 = (SVGGroupElem)svgdocument7.linkTargets.data[svgdocument7.linkIndex];
            if(svggroupelem2 != null)
            {
                SVGEvent svgevent13 = new SVGEvent(6, svggroupelem2);
                canvas.postEvent(svgevent13);
            }
            break;

        case 8: // '\b'
            SVGGroupElem svggroupelem3 = (SVGGroupElem)svgevent.data;
            if(svggroupelem3 != null)
            {
                svggroupelem3.showBounds = false;
                SVGEvent svgevent14 = new SVGEvent(19, svggroupelem3.getDevBounds(canvas.raster));
                canvas.postEvent(svgevent14);
            }
            break;

        case 9: // '\t'
            SVGDocument svgdocument8 = canvas.raster.document;
            if(svgdocument8.linkTargets.count == 0)
            {
                return;
            }
            doLink((SVGGroupElem)svgdocument8.linkTargets.data[svgdocument8.linkIndex]);
            break;

        case 10: // '\n'
            SVGDocument svgdocument9 = canvas.raster.document;
            if(svgdocument9.linkTargets.count == 0)
            {
                return;
            }
            if(svgdocument9.linkIndex == 0)
            {
                return;
            }
            SVGGroupElem svggroupelem4 = (SVGGroupElem)svgdocument9.linkTargets.data[svgdocument9.linkIndex];
            if(svggroupelem4 != null)
            {
                SVGEvent svgevent15 = new SVGEvent(8, svggroupelem4);
                canvas.postEvent(svgevent15);
            }
            svgdocument9.linkIndex--;
            svggroupelem4 = (SVGGroupElem)svgdocument9.linkTargets.data[svgdocument9.linkIndex];
            if(svggroupelem4 != null)
            {
                SVGEvent svgevent16 = new SVGEvent(6, svggroupelem4);
                canvas.postEvent(svgevent16);
            }
            break;

        case 11: // '\013'
            SVGDocument svgdocument10 = canvas.raster.document;
            svgdocument10.linkTargets.count = 0;
            svgdocument10.linkIndex = 0;
            svgdocument10.addLinks(svgdocument10.root);
            if(svgdocument10.linkTargets.count == 0)
            {
                return;
            }
            SVGGroupElem svggroupelem5 = (SVGGroupElem)svgdocument10.linkTargets.data[svgdocument10.linkIndex];
            if(svggroupelem5 != null)
            {
                SVGEvent svgevent17 = new SVGEvent(6, svggroupelem5);
                canvas.postEvent(svgevent17);
            }
            break;
        }
    }

}
