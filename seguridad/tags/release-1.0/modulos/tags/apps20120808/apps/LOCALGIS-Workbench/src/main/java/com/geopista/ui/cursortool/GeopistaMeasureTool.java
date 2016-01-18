package com.geopista.ui.cursortool;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.geopista.app.AppContext;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.cursortool.MultiClickTool;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.text.DecimalFormat;
import javax.swing.Icon;

/** Clase implementada para sustituir a MeasureTools, hasta que sepamos porque necesita llamar
 * en el constructor al metodo que falla
 */

public class GeopistaMeasureTool extends MultiClickTool {
    
    AppContext appContext=(AppContext) AppContext.getApplicationContext();
    
    public GeopistaMeasureTool() {
        allowSnapping();
    }

    public Icon getIcon() {
        return IconLoader.icon("Ruler.gif");
    }

    public Cursor getCursor() {
        return createCursor(IconLoader.icon("RulerCursor.gif").getImage());
    }

    public void mouseLocationChanged(MouseEvent e) {
        try {
            if (isShapeOnScreen()) {
                ArrayList currentCoordinates = new ArrayList(getCoordinates());
                currentCoordinates.add(getPanel().getViewport().toModelCoordinate(e.getPoint()));
                display(currentCoordinates, getPanel());
            }

            super.mouseLocationChanged(e);
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }

    protected void gestureFinished() throws NoninvertibleTransformException {
        reportNothingToUndoYet();

        //Status bar is cleared before #gestureFinished is called. So redisplay
        //the length. [Jon Aquino]
        display(getCoordinates(), getPanel());
    }

    private void display(List coordinates, LayerViewPanel panel)
        throws NoninvertibleTransformException {
        display(distance(coordinates), panel,getArea(coordinates));
    }

    private void display(double distance, LayerViewPanel panel, double area) {
        
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
        
        
        panel.getContext().setStatusMessage(appContext.getI18nString("GeopistaMeasureTool.Distance") +
            " "+panel.format(distance)+"  " +appContext.getI18nString("GeopistaMeasureTool.Area")+" "+areaString);
    }

    private double distance(List coordinates) {
        double distance = 0;

        for (int i = 1; i < coordinates.size(); i++) {
            distance += ((Coordinate) coordinates.get(i - 1)).distance((Coordinate) coordinates.get(
                    i));
        }

        return distance;
    }
    
    private double getArea(List sourceCoordinates)
    {
        
        List coordinates = new ArrayList(sourceCoordinates);
        if(coordinates.size()>=3)
        {
	        GeometryFactory factory = new GeometryFactory();
	        
	        Iterator coordinatesIter = coordinates.iterator();

	        if(coordinates.get(0)!=coordinates.get(coordinates.size()-1))
	        {
	            coordinates.add(coordinates.get(0));
	        }
	        
	        Object[] objectArray = coordinates.toArray(new Coordinate[coordinates.size()]);
	        
	        LinearRing multi = factory.createLinearRing((Coordinate[]) objectArray);
	        Polygon poligono = factory.createPolygon(multi, null); 
	        
	        return poligono.getArea();
        }
        else
        {
            return 0.0;
        }
    }
}
