package com.localgis.app.gestionciudad.plugins.geomarketing.utils;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import com.geopista.ui.cursortool.DragTool;
import com.localgis.app.gestionciudad.plugins.geomarketing.CircleInformationGeoMarketingPlugIn;
import com.localgis.app.gestionciudad.plugins.geomarketing.images.IconLoader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;


public class SelectItemsByCircleToolGeoMarketing extends DragTool {
	
    private Shape selectedFeaturesShape;
    private GeometryFactory geometryFactory = new GeometryFactory();
    private WorkbenchContext contextWorkbench = null;
    private double radius=50;
    private Point mp = null;
    private Geometry circleGeometry = null;
    private boolean active = false;
    
   
    public SelectItemsByCircleToolGeoMarketing(WorkbenchContext context,double radius) {
    	this.radius = radius;
    	this.contextWorkbench =context;
    	
//    	Locale loc=Locale.getDefault();
//    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.selectitemsbycirclefromselectedlayers.languages.SelectItemsByCircleFromSelectedLayersPlugIni18n",loc,this.getClass().getClassLoader());    	
//        I18N.plugInsResourceBundle.put("SelectItemsByCircleFromSelectedLayersPlugIn",bundle2);


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
    
//    public String getName(){
//    	String name = I18N.get("SelectItemsByCircleFromSelectedLayersPlugIn","SelectItemsByCircleFromSelectedLayersPlugIn");
//    	return name;
//    }
    
    public Icon getIcon() {
        return IconLoader.icon("selecteditemsbycircle.gif");
    }

    /******************  events ********************************/
    protected void gestureFinished() throws java.lang.Exception {
        reportNothingToUndoYet();    
        CircleInformationGeoMarketingPlugIn.onCircleSelectFinish();
    }
    
    public void activate(LayerViewPanel layerViewPanel) {
    	this.active = true;
    	super.activate(layerViewPanel);
    	
//    	try
//        {        
//        		
        	if(this.radius > 0){
        		Envelope viewportEnvelope = getPanel().getViewport().getEnvelopeInModelCoordinates();
	        	double x = viewportEnvelope.getMinX() + viewportEnvelope.getWidth()/2;
	        	double y = viewportEnvelope.getMinY() + viewportEnvelope.getHeight()/2;
	        	Coordinate initCoords = new Coordinate(x,y);
	        	this.calcuateCircle(initCoords);
        	}                        
//        }
//        catch (Exception e)
//        {            
//            return;
//        }
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
        	if (circle != null){
        		this.circleGeometry = circle;
        	}
//            this.selectItems(circle);
            fireGestureFinished();      	
        } 
        catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
        
        this.deactivate();
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
    	if (this.isActive()){
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
    }
    
//    /**
//     * called on mouse click<p>
//     * selects all the items from the selected layer 
//     * wich are not(!) disjoint with circle 
//     */
//    private void selectItems(Geometry circle){
//		int count = 0;
//		Layer[] selectedLayers = contextWorkbench.getLayerNamePanel().getSelectedLayers();
//		Collection features = new ArrayList();
//		for (int i = 0; i < selectedLayers.length; i++) {
//					
//			if (!wasShiftPressed()) {
//	            getPanel().getSelectionManager().clear();
//	        }
//			
//			Layer actualLayer = selectedLayers[i]; 
//			FeatureCollection fc = actualLayer.getFeatureCollectionWrapper().getWrappee();
//			
//			for (Iterator iter = fc.iterator(); iter.hasNext();) {
//				Feature element = (Feature) iter.next();
//				if(!circle.disjoint(element.getGeometry())){
//					features.add(element);
//					count++;
//				}
//			}
//			contextWorkbench.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(actualLayer, features);
//						
//		}			    
//
//    }
    
    public Geometry getCircleGeometry(){
    	return this.circleGeometry;
    }
    
    public boolean isActive(){
    	return this.active;
    }
    
    
    @Override
    public void deactivate(){
    	this.active = false;
        super.deactivate();
    }
    
}
