package com.geopista.ui.plugin.editing.helpclassesselection;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Icon;
import com.geopista.app.AppContext;
import com.geopista.ui.cursortool.DragTool;
import com.geopista.ui.plugin.selectitemsbycirclefromselectedlayers.images.IconLoader;
import com.vividsolutions.jts.geom.Coordinate; 
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;


public class SelectItemsByCircleTool extends DragTool {
	
    private Shape selectedFeaturesShape;
    private GeometryFactory geometryFactory = new GeometryFactory();
    private WorkbenchContext contextWorkbench = null;
    private double radius=50;
    private Point mp = null;
    
    private static String T1 = "";
    private static String sidebarstring ="";
   
    public SelectItemsByCircleTool(WorkbenchContext context) {
    	
    	this.contextWorkbench =context;
    	
    	Locale loc=Locale.getDefault();
    	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.selectitemsbycirclefromselectedlayers.languages.SelectItemsByCircleFromSelectedLayersPlugIni18n",loc,this.getClass().getClassLoader());
    	
        I18N.plugInsResourceBundle.put("SelectItemsByCircleFromSelectedLayersPlugIn",bundle2);
    	
        this.T1 = I18N.get("SelectItemsByCircleFromSelectedLayersPlugIn","SelectItemsByCircleFromSelectedLayers.Radius") + ":";
        this.sidebarstring = I18N.get("SelectItemsByCircleFromSelectedLayersPlugIn","SelectItemsByCircleFromSelectedLayers.Message");

        setStroke(
            new BasicStroke(
                1,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL,
                0,
                new float[] { 3, 3 },
                0));
        this.allowSnapping();  
        
    }
    
    public String getName(){
    	String name = I18N.get("SelectItemsByCircleFromSelectedLayersPlugIn","SelectItemsByCircleFromSelectedLayersPlugIn");
    	return name;
    }
    
    public Icon getIcon() {
        return IconLoader.icon("selecteditemsbycircle.gif");
    }

    /******************  events ********************************/
    protected void gestureFinished() throws java.lang.Exception {
        reportNothingToUndoYet();              
    }
    
    public void activate(ILayerViewPanel layerViewPanel) {
    	
    	super.activate(layerViewPanel);
    	
    	try
        {        
        		
        	if(this.makeDialogThings(contextWorkbench)){
        		Envelope viewportEnvelope = getPanel().getViewport().getEnvelopeInModelCoordinates();
	        	double x = viewportEnvelope.getMinX() + viewportEnvelope.getWidth()/2;
	        	double y = viewportEnvelope.getMinY() + viewportEnvelope.getHeight()/2;
	        	Coordinate initCoords = new Coordinate(x,y);
	        	this.calcuateCircle(initCoords);
        	}                        
        }
        catch (Exception e)
        {            
            return;
        }
    }
    
    protected void reportNothingToUndoYet(WorkbenchContext context) {
        //The LayerManager can be null if for example there are no TaskFrames and
        //the user selects File / New Task. When we get to this point, LayerManager
        //will be null. [Jon Aquino]
        if (context.getLayerManager() == null) {
            return;
        }
        context
            .getLayerManager()
            .getUndoableEditReceiver()
            .reportNothingToUndoYet();
    }
    
    public boolean makeDialogThings(WorkbenchContext context) throws Exception{
    		   
	    MultiInputDialog dialog = new MultiInputDialog(AppContext.getApplicationContext().getMainFrame(),getName(),true);
	    	
	    	dialog.setSize(50, 50);	    	
	        dialog.setResizable(false);  // Generated
	        dialog.setLocationRelativeTo(null);
	        setDialogValues(dialog, context);
	        dialog.setVisible(true);	     
	        if (! dialog.wasOKPressed()) { return false; }
	        getDialogValues(dialog);
	        return true;	
	}
    
    private void getDialogValues(MultiInputDialog dialog) {
	    this.radius = dialog.getDouble(T1);
	   
	  }
    
    private void setDialogValues(MultiInputDialog dialog, WorkbenchContext context)
	  {   
	    dialog.setSideBarDescription(this.sidebarstring);    	
	    dialog.addDoubleField(T1,this.radius,7,T1);    	
	  }
   
    public Cursor getCursor() {
        return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    }
    	
    public void mouseMoved(MouseEvent e){
        try {
            setViewDestination(e.getPoint());
            redrawShape();
            } 
        catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }
    
    public void mouseReleased(MouseEvent e) {
    	super.mouseReleased(e);    	
    }

    public void mousePressed(MouseEvent e) {
        try {
        	
        	super.mousePressed(e);
        	GeometryFactory a = new GeometryFactory();
        	Point p = a.createPoint(this.getModelDestination());
        	
        	this.mp = p;
        	Geometry circle = mp.buffer(this.radius);   	
            this.selectItems(circle);
            fireGestureFinished();      	
        } 
        catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }
    
    protected Shape getShape() throws Exception {
    	this.calcuateCircle(this.modelDestination);
		return this.selectedFeaturesShape; 
    }
    
    protected void setModelDestination(Coordinate modelDestination) {
    	    	
        this.modelDestination = modelDestination;

    }
     
	/**
	 * called from constructor and by mouse move event<p>
	 * calculates a cirle around the mouse pointer and converts it to a java shape  
	 * @param middlePoint coordinates of the circle
	 */
    private void calcuateCircle(Coordinate middlePoint){
        //--calcualte circle;
    	Point p = new GeometryFactory().createPoint(middlePoint);
    	this.mp = p;
    	Geometry buffer = p.buffer(this.radius);   	

    	Geometry[] geomArray = new Geometry[1];
    	geomArray[0] = buffer;
    	GeometryCollection gc = geometryFactory.createGeometryCollection(geomArray);
    	try{
    		this.selectedFeaturesShape = this.contextWorkbench.getLayerViewPanel().getJava2DConverter().toShape(gc);
    	}    	
    	catch(Throwable t) {
            getPanel().getContext().handleThrowable(t);
    	}
    }
    
    /**
     * called on mouse click<p>
     * selects all the items from the selected layer 
     * wich are not(!) disjoint with circle 
     */
    private void selectItems(Geometry circle){
		int count = 0;
		Layer[] selectedLayers = contextWorkbench.getLayerNamePanel().getSelectedLayers();
		Collection features = new ArrayList();
		for (int i = 0; i < selectedLayers.length; i++) {
					
			if (!wasShiftPressed()) {
	            getPanel().getSelectionManager().clear();
	        }
			
			Layer actualLayer = selectedLayers[i]; 
			FeatureCollection fc = actualLayer.getFeatureCollectionWrapper().getWrappee();
			
			for (Iterator iter = fc.iterator(); iter.hasNext();) {
				Feature element = (Feature) iter.next();
				if(!circle.disjoint(element.getGeometry())){
					features.add(element);
					count++;
				}
			}
			contextWorkbench.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(actualLayer, features);
						
		}			    

    }
}
