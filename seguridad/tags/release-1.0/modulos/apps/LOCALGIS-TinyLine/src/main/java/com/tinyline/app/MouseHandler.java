package com.tinyline.app;

import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGRaster;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyPoint;
import java.awt.*;
import java.awt.event.*;

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
