
/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.vividsolutions.jump.workbench.ui.zoom;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import com.vividsolutions.jump.feature.*;
import com.vividsolutions.jts.geom.Geometry;

import javax.swing.Icon;


import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.workbench.ui.cursortool.DragTool;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;


public class PanTool extends DragTool {
    private boolean dragging = false;
    private Image image;

    public PanTool() {
    }

    public Cursor getCursor() {
        return createCursor(IconLoader.icon("Hand.gif").getImage());
    }

    public Icon getIcon() {
        return IconLoader.icon("BigHand.gif");
    }

    public void mouseDragged(MouseEvent e) {
        try {
            if (!dragging) {
                dragging = true;
                getPanel().getRenderingManager().setPaintingEnabled(false);
                cacheImage();
            }

            getPanel().erase((Graphics2D) getPanel().getGraphics());
            drawImage(e.getPoint());
            super.mouseDragged(e);
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (!dragging) {
            return;
        }

        getPanel().getRenderingManager().setPaintingEnabled(true);
        dragging = false;
        super.mouseReleased(e);
    }

    protected Shape getShape(Point2D source, Point2D destination) {
        return null;
    }

    protected void gestureFinished() throws NoninvertibleTransformException {
        reportNothingToUndoYet();

        double xDisplacement = getModelDestination().x - getModelSource().x;
        double yDisplacement = getModelDestination().y - getModelSource().y;
        Envelope oldEnvelope = getPanel().getViewport()
                                   .getEnvelopeInModelCoordinates();
        getPanel().getViewport().zoom(new Envelope(oldEnvelope.getMinX() -
                xDisplacement, oldEnvelope.getMaxX() - xDisplacement,
                oldEnvelope.getMinY() - yDisplacement,
                oldEnvelope.getMaxY() - yDisplacement));
    }

    private void cacheImage() {
        image = getPanel().createBlankPanelImage();
        getPanel().paint(image.getGraphics());
    }

    private void drawImage(Point p) throws NoninvertibleTransformException {
        double dx = p.getX() - getViewSource().getX();
        double dy = p.getY() - getViewSource().getY();
        getPanel().getGraphics().drawImage(image, (int) dx, (int) dy, getPanel());
    }

    public void panTo(Feature feature) throws NoninvertibleTransformException
    {
     /*
      com.vividsolutions.jts.geom.Point centroide = feature.getGeometry().getCentroid();
     

      double centroX = centroide.getX();
      double centroY = centroide.getY();      

      System.out.println("getpanel    : "+getPanel());
      System.out.println("getviewport :  "+getPanel().getViewport());
      System.out.println("getmodelincoordinates :   "+getPanel().getViewport().getEnvelopeInModelCoordinates());
    
      Envelope oldEnvelope = getPanel().getViewport().getEnvelopeInModelCoordinates();

      double centroideEnvelopeX = (oldEnvelope.getMaxX() - oldEnvelope.getMinX())/2;
      double centroideEnvelopeY = (oldEnvelope.getMaxY() - oldEnvelope.getMinY())/2;

      double xDisplacement = centroX - centroideEnvelopeX;
      double yDisplacement = centroY - centroideEnvelopeY;

     
      getPanel().getViewport().zoom(new Envelope(oldEnvelope.getMinX() - xDisplacement, oldEnvelope.getMaxX() - xDisplacement, oldEnvelope.getMinY() - yDisplacement, oldEnvelope.getMaxY() - yDisplacement));
      */
      
    }
}
