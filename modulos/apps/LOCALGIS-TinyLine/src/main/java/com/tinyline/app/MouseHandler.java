/**
 * MouseHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.app;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyPoint;

// Referenced classes of package com.tinyline.app:
//            SVGEvent, PPSVGCanvas

public class MouseHandler implements MouseListener, MouseMotionListener
{

    PPSVGCanvas canvas;
    public static final int LINK_MOUSE = 0;
    public static final int PAN_MOUSE = 1;
    public static final int ZOOM_IN_MOUSE = 2;
    public static final int ZOOM_OUT_MOUSE = 3;
    public int type;
    int pressedX;
    int pressedY;
    int draggedX;
    int draggedY;

    public MouseHandler(PPSVGCanvas ppsvgcanvas)
    {
        type = 0;
        canvas = ppsvgcanvas;
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        if(type == 1)
        {
            pressedX = mouseevent.getX();
            pressedY = mouseevent.getY();
            draggedX = pressedX;
            draggedY = pressedY;
        }
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        if(type == 3)
        {
            SVGEvent svgevent = new SVGEvent(20, new TinyNumber(1));
            canvas.postEvent(svgevent);
        } else
        if(type == 2)
        {
            SVGEvent svgevent1 = new SVGEvent(20, new TinyNumber(0));
            canvas.postEvent(svgevent1);
        } else
        if(type == 0)
        {
            SVGEvent svgevent2 = new SVGEvent(2, new TinyPoint(mouseevent.getX(), mouseevent.getY()));
            canvas.postEvent(svgevent2);
        } else
        if(type == 1)
        {
            SVGEvent svgevent3 = new SVGEvent(17, new TinyPoint(pressedX - mouseevent.getX(), pressedY - mouseevent.getY()));
            canvas.postEvent(svgevent3);
        }
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
        if(type == 1 && canvas.raster.document.isZoomAndPanAnable())
        {
            drawXORLine(pressedX, pressedY, draggedX, draggedY);
            draggedX = mouseevent.getX();
            draggedY = mouseevent.getY();
            drawXORLine(pressedX, pressedY, draggedX, draggedY);
        }
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
    }

    public void drawXORLine(int i, int j, int k, int l)
    {
        Graphics g = canvas.getGraphics();
        if(g != null)
        {
            g.setXORMode(Color.white);
            g.setColor(Color.black);
            g.drawLine(i, j, k, l);
            g.dispose();
            g = null;
        }
    }
}
