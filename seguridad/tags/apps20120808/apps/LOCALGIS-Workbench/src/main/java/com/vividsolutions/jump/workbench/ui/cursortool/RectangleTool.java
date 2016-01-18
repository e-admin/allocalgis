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

package com.vividsolutions.jump.workbench.ui.cursortool;

import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.editor.WorkbenchGuiComponent;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.geom.CoordUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

public abstract class RectangleTool extends DragTool {
    
	private List coordinates = new ArrayList();
	
	public RectangleTool() {
    }

    protected Polygon getRectangle() throws NoninvertibleTransformException {
        Envelope e = new Envelope(
                getModelSource().x,
                getModelDestination().x,
                getModelSource().y,
                getModelDestination().y);

        return new GeometryFactory().createPolygon(
            new GeometryFactory().createLinearRing(
                new Coordinate[] {
                    new Coordinate(e.getMinX(), e.getMinY()),
                    new Coordinate(e.getMinX(), e.getMaxY()),
                    new Coordinate(e.getMaxX(), e.getMaxY()),
                    new Coordinate(e.getMaxX(), e.getMinY()),
                    new Coordinate(e.getMinX(), e.getMinY())}),
            null);
    }

    private Collection verticesToSnap(Coordinate source, Coordinate destination) {
        ArrayList verticesToSnap = new ArrayList();
        verticesToSnap.add(destination);
        verticesToSnap.add(new Coordinate(source.x, destination.y));
        verticesToSnap.add(new Coordinate(destination.x, source.y));

        return verticesToSnap;
    }

    protected void setModelDestination(Coordinate modelDestination) {
        for (Iterator i = verticesToSnap(getModelSource(), modelDestination).iterator(); i.hasNext();) {
            Coordinate vertex = (Coordinate) i.next();
            Coordinate snappedVertex = snap(vertex);

            if (getSnapManager().wasSnapCoordinateFound()) {
                this.modelDestination = CoordUtil.add(modelDestination, CoordUtil.subtract(snappedVertex, vertex));
                return;
            }

        }
        this.modelDestination = modelDestination;
        return;
    }
    protected void setModelSource(Coordinate modelSource) {
        this.modelSource = snap(modelSource);
    }
    
    
    /**
     * Will return an empty List once the shape is cleared.
     * @see MultiClickTool#clearShape
     */
    public List getCoordinates() {
        return Collections.unmodifiableList(coordinates);
    }
      
    public void mousePressed(MouseEvent e) {
    	super.mousePressed(e);
    	coordinates.clear();
    	
    	    	
    	try {
			coordinates.add(getPanel().getViewport().toModelCoordinate(e.getPoint() ));
			coordinates.add(getPanel().getViewport().toModelCoordinate(e.getPoint() ));
		} catch (NoninvertibleTransformException e1) {
			e1.printStackTrace();
		}
     }
    
    public void mouseDragged(MouseEvent e) {
    	
    	super.mouseDragged(e);
    	double area, distance;

    	//Voy variando la segunda coordenada a medida que voy arrastrando el raton
    	try {
			coordinates.set(1, getPanel().getViewport().toModelCoordinate(e.getPoint()));
		} catch (NoninvertibleTransformException e1) {
			e1.printStackTrace();
		}
    	
    	area = getArea(coordinates);
    	distance=getDistance(coordinates);
    	
    	display(distance,getPanel(),area);
    }
    
    private double getDistance(List Coordinates)
    {
    	Coordinate a,b;
    	double distance;
    	
    	a = (Coordinate)Coordinates.get(0);
    	b = (Coordinate)Coordinates.get(1);
    	
    	//Perímetro del Rectángulo
    	distance=  2*Math.abs(b.x-a.x) + 2*Math.abs(b.y -a.y);
    	return distance;
    }
    
    private double getArea(List Coordinates)
	{
    	Coordinate a,b;
    	double area;
    	
    	a = (Coordinate)Coordinates.get(0);
    	b = (Coordinate)Coordinates.get(1);
    	
    	area=(b.x-a.x)*(b.y-a.y);
    	
    	if (area<0){
    		area = 0-area;	
    	}
    	return area;
	}
    
    
	/**
	 * @param distance
	 * @param panel
	 * @param area
	 */
	private void display(double distance, LayerViewPanel panel, double area) {
	    
		AppContext appContext = (AppContext) AppContext.getApplicationContext();
		
		String unitName = appContext.getUserPreference("unidad.nombre","",true);
	    String unitEquivalence = appContext.getUserPreference("unidad.equivalence","",true);
	    String areaString = null;
	    
	    DecimalFormat decimalFormat = new DecimalFormat();
	    decimalFormat.setMaximumFractionDigits(3);
	    String parseArea = decimalFormat.format(area);
	    
		if(unitEquivalence!=null&&!unitEquivalence.equals(""))
	    {
	        String parseEquivalence = decimalFormat.format(area/(Double.valueOf(unitEquivalence).doubleValue()));
			areaString = parseArea+" m2 ( "+parseEquivalence+" "+unitName+" ) ";
	    }
	    else
	    {
	        areaString = parseArea +" m2 ";
	    }
 
		if (getPanel().getContext() instanceof GeopistaEditor){
			((GeopistaEditor)getPanel().getContext()).setTimeMessage(I18N.get("GeopistaMeasureTool.Distance") +
					" "+panel.format(distance)+"  " +I18N.get("GeopistaMeasureTool.Area")+" "+areaString);
		}
		else if (getPanel().getContext() instanceof WorkbenchGuiComponent){
			((WorkbenchGuiComponent)getPanel().getContext()).setTimeMessage(I18N.get("GeopistaMeasureTool.Distance") +
					" "+panel.format(distance)+"  " +I18N.get("GeopistaMeasureTool.Area")+" "+areaString);
		}
		
	}  

}
