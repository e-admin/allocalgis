/**
 * RotateSelectedItemsTool.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.cursortool.rotateselecteditems;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.ui.cursortool.DragTool;
import com.geopista.ui.cursortool.rotateselecteditems.images.IconLoader;
import com.geopista.ui.geoutils.GeoUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.ViewportListener;
import com.vividsolutions.jump.workbench.ui.snap.SnapManager;

public class RotateSelectedItemsTool extends DragTool {
    private EnableCheckFactory checkFactory;
    private Shape selectedFeatureShape;    
    private Coordinate centerCoord;
    protected boolean clockwise = true;
    private double fullAngle = 0.0;
    
    private Coordinate mousePos = null;
    double toleranceFactor = 2.0;
    private Geometry buffer=null;    
    private Point mp = null;
    private PlugInContext context = null;
	private boolean rotating = false;
	private double anglePanel = 0.0;
	
   
    public RotateSelectedItemsTool(EnableCheckFactory checkFactory) {
        this.checkFactory = checkFactory;
        
        setStroke(
            new BasicStroke(
                1,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL,
                0,
                new float[] { 3, 3 },
                0));
        allowSnapping();
        
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.cursortool.rotateselecteditems.languages.RotateSelectedItemsTooli18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("RotateSelectedItemsTool",bundle2);
    }
    
    public String getName(){
    	String name = I18N.get("RotateSelectedItemsTool","RotateSelectedItems");
    	return name;
    }

    protected void gestureFinished() throws java.lang.Exception {
        reportNothingToUndoYet();
        if(rotating == true){
	        ArrayList transactions = new ArrayList();
	        for (Iterator i = getPanel().getSelectionManager().getLayersWithSelectedItems().iterator();
	            i.hasNext();
	            ) {
	            Layer layerWithSelectedItems = (Layer) i.next();
	            transactions.add(createTransaction(layerWithSelectedItems));
	        }
	        EditTransaction.commit(transactions);
	        
	        selectedFeatureShape = null;
	        centerCoord = null;
	        rotating = false;
	        buffer = null;
        }
    }

    private EditTransaction createTransaction(Layer layer) {
        EditTransaction transaction =
            EditTransaction.createTransactionOnSelection(new EditTransaction.SelectionEditor() {
            public Geometry edit(Geometry geometryWithSelectedItems, Collection selectedItems) {
                for (Iterator j = selectedItems.iterator(); j.hasNext();) {
                    Geometry item = (Geometry) j.next();
                    rotate(item);
                }
                return geometryWithSelectedItems;
            }
        }, getPanel(), getPanel().getContext(), getName(), layer, isRollingBackInvalidEdits(), false);
        return transaction;
    }

    private void rotate(Geometry geometry) {
        geometry.apply(new CoordinateFilter() {
            public void filter(Coordinate coordinate) {
                double cosAngle = Math.cos(fullAngle);
                double sinAngle = Math.sin(fullAngle);
                double x = coordinate.x - centerCoord.x;
                double y = coordinate.y - centerCoord.y;
                coordinate.x = centerCoord.x + (x*cosAngle) + (y*sinAngle);
                coordinate.y = centerCoord.y + (y*cosAngle) - (x*sinAngle);
              }
        });
    }
    
    public Cursor getCursor() {
    	return Toolkit.getDefaultToolkit().createCustomCursor(
                IconLoader.icon("RotateSelCursor.gif").getImage(),
                new java.awt.Point(16, 16),
                getName());
    
    }

    public Icon getIcon() {
    	return IconLoader.icon("RotateSel.gif");        
    }

    public void mousePressed(MouseEvent e) {
        try {
                        
        	if (!checkLayersSelectedIsEditable("RotateSelectedItemsTool")){
                return;
            }
            
        	if(!checkAtLeastNItemsMustBeSelected(1,"RotateSelectedItemsTool")){
                return;
            }
            rotating = false;
            selectedFeatureShape = createSelectedItemsShape();
            
            super.mousePressed(e);
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }

	public void mouseClicked(MouseEvent e) {
    	
		if(rotating == false){
	    	try{
	    		
	    		this.selectedFeatureShape = null;
	    		
	    		this.setMousePos(getPanel().getViewport().toModelCoordinate(e.getPoint())); //-- includes snap
	   		
	    		double tolerance = SnapManager.getToleranceInPixels(this.getPanel().getBlackboard()) / this.getPanel().getViewport().getScale();
	    		
	    		ArrayList listCenters = new ArrayList();
	    		//Obtener las geometrías de las features que se encuentren debajo del ratón
	    		GeometryFactory a = new GeometryFactory();
	        	Point p = a.createPoint(this.mousePos);
	        	
	        	this.mp = p;
	        	Geometry circle = mp.buffer(tolerance*this.toleranceFactor);   	
	            Collection selectedFeatures = this.selectItems(circle);
	            
	            for(Iterator i=selectedFeatures.iterator();i.hasNext();){
	            	Geometry ls = ((Feature)i.next()).getGeometry();
	        		        		
	        		MultiPoint mps = new GeometryFactory().createMultiPoint(ls.getCoordinates());
	        		Geometry buffergeom = mps.buffer(tolerance*this.toleranceFactor);
	        		Point mousep = new GeometryFactory().createPoint(this.mousePos);
	        		    		
	            	if (buffergeom.contains(mousep)){
	            		//Obtener el punto del item seleccionado más próximo
	            		Coordinate center = this.getNearestPoint(ls, this.mousePos);
	            		
	            		listCenters.add(center);
	    	    	   	       	    	   		    		
	            	}      
	            }
	            double distance = 0;
	            
	            boolean init = true;
	            Coordinate centerNearest = null;
	            for(Iterator j=listCenters.iterator();j.hasNext();){
	            	Coordinate centerTemp = (Coordinate)j.next();
	            	if(init){
	            		distance = centerTemp.distance(this.mousePos);
	            		centerNearest = centerTemp;
	            		init = false;
	            	}
	            	else{
	            		if(distance>centerTemp.distance(this.mousePos)){
	                		distance = centerTemp.distance(this.mousePos);
	                		centerNearest = centerTemp;
	                	} 
	            	} 
	            }
	            this.centerCoord = centerNearest;
	            
	            
	            if(this.centerCoord!=null){
		            Point point = new GeometryFactory().createPoint(this.centerCoord);
		            this.buffer = point.buffer(1*tolerance/2);
		            
				    this.fullAngle = 0.0;
				    setViewSource(e.getPoint());
				    setViewDestination(e.getPoint());
				    selectedFeatureShape = createSelectedItemsShape();				    
				    this.redrawShape(); 
	            }
	    			        	
	    	}
	        catch (Throwable t) {
	        	getPanel().getContext().handleThrowable(t);
	        }
		}
    }
	
	protected void setMousePos(Coordinate destination) {
        this.mousePos = snap(destination);
   } 

    private Shape createSelectedItemsShape() throws NoninvertibleTransformException {
        Collection selectedGeos = (getPanel().getSelectionManager().getSelectedItems());
        GeometryFactory gf = new GeometryFactory();        
        Geometry geo = gf.buildGeometry(selectedGeos);
        if(centerCoord==null){
        	centerCoord = geo.getCentroid().getCoordinate(); 
        }
        else{        	
        	if(this.buffer != null){
        		selectedGeos.add(this.buffer);
        		geo = gf.buildGeometry(selectedGeos);
        	}
        }
        return getPanel().getJava2DConverter().toShape(geo);
    }

    protected Shape getShape() throws Exception {
    	
    	if(selectedFeatureShape!=null){
	    	AffineTransform transform = new AffineTransform();
	    	        
	        Point2D centerPt = getPanel().getViewport().toViewPoint(new Point2D.Double(centerCoord.x, centerCoord.y));
	        Point2D initialPt = getViewSource();
	        Point2D currPt = getViewDestination();
	        MathVector center = new MathVector(centerPt.getX(), centerPt.getY());
	        MathVector initial = new MathVector(initialPt.getX(), initialPt.getY());
	        MathVector curr = new MathVector(currPt.getX(), currPt.getY());
	        MathVector initVec = initial.vectorBetween(center);
	        MathVector currVec = curr.vectorBetween(center);
	        double arcAngle = initVec.angleRad(currVec);
	        
	        if(anglePanel!=0){
	        	arcAngle = Math.toRadians(anglePanel);
	        }
	        
	        Coordinate initialCoord = getPanel().getViewport().toModelCoordinate(initialPt);
	        Coordinate currCoord = getPanel().getViewport().toModelCoordinate(currPt);
	        
	        boolean toRight = (new GeoUtils().pointToRight(currCoord, centerCoord, initialCoord));      
	        boolean cwQuad = ((fullAngle >= 0.0) &&(fullAngle <= 90.0) && clockwise);
	        boolean ccwQuad = ((fullAngle < 0.0) &&(fullAngle >= -90.0) && !clockwise);
	                
	                
	        if ((arcAngle <= 90.0) && (cwQuad || ccwQuad))
	        {
	            if (toRight)
	                clockwise = true;
	            else
	                clockwise = false;
	        }
	
	        if ((fullAngle > 90.0) || (fullAngle < -90))
	        {
	            if ((clockwise && !toRight) || (!clockwise && toRight))
	                fullAngle = 360 - arcAngle;
	            else
	                fullAngle = arcAngle;
	        }
	        else
	        {
	            fullAngle = arcAngle;
	        }
	        
	        if(Double.isNaN(fullAngle)){
	        	fullAngle=0;
	        	
	        }
	        
	        if (!clockwise)
	            fullAngle = -fullAngle;
	
	        DecimalFormat df2 = new DecimalFormat("##0.0#");
	        getPanel().getContext().setStatusMessage(I18N.get("RotateSelectedItemsTool","angle") + " = " + df2.format(Math.toDegrees(fullAngle)) + " " + I18N.get("RotateSelectedItemsTool","degrees"));
	        
	        transform.rotate(fullAngle, centerPt.getX(), centerPt.getY());
	    	
	        return transform.createTransformedShape(selectedFeatureShape);
    	}
    	else{
    		return null;
    	}
    	
   }
    
    public void cancelGesture() {
        
        super.cancelGesture();  
        selectedFeatureShape = null;
        centerCoord = null;
        rotating = false;
        buffer = null;
        removePanel(toolbox,addPanel,initialSize); 
        getPanel().getContext().setStatusMessage("");
    }
    
    public void activate(ILayerViewPanel panel){
    	super.activate(panel);
    	
    	panel.getViewport().addListener(new ViewportListener(){
			public void zoomChanged(Envelope modelEnvelope) {
				selectedFeatureShape = null;			
			}    			
    	});
    	
    	//Añadir el panel con el factor de escalado al toolbox
    	toolbox = getPanelToolBox();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
        JLabel angle = new JLabel(I18N.get("RotateSelectedItemsTool","Angle"));
        final JTextField angleRotation = new JTextField(8);
        angleRotation.setName("anglerotation");
        angleRotation.setText("0,0");
        
        addPanel = new JPanel();
        addPanel.setLayout(gridBagLayout);
                        
        angleRotation.addKeyListener(new KeyAdapter()
        {
           public void keyTyped(KeyEvent e)
           {
              char caracter = e.getKeyChar();	              
              if(((caracter < '0') || (caracter > '9')) &&
                      (caracter != KeyEvent.VK_BACK_SPACE) && (caracter != ',') && (caracter != '-'))
              {
            	  e.consume();               
              }        	     
           }
           
           public void keyPressed(KeyEvent e)
           {
        	   int caracter = e.getKeyChar();	        	   
        	   
        	   if(caracter==KeyEvent.VK_ENTER){		            	  
        		   try{	        			  	        			   
        			          			   
        			   double angle = readAngleRotation();        			  
        			   RotateSelectedItemsTool.this.clickReturn(angle);
			          
		           }catch(Throwable t){
	            		  getPanel().getContext().handleThrowable(t);
        		   }
	            }	       
        	   
           }
           	           
           public void keyReleased(KeyEvent e)
           {	        	  
    			   try{	 	    	
    				   
    				   MouseEvent event = new MouseEvent(e.getComponent(),
        	 	               MouseEvent.MOUSE_CLICKED,
        	 	               System.currentTimeMillis(), e.getModifiers(),
        	 	               0, 0,1,
        	 	               false);
    				  double factorScale = readAngleRotation();	        			  
        			  RotateSelectedItemsTool.this.angleRotationChanged(factorScale,event);	
        			          			  
		           }catch(Throwable t){
	            		  getPanel().getContext().handleThrowable(t);
        		   }	    		      	   
           }	           
        });
        addPanel.add(angle, new GridBagConstraints(
                0,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2),
                0,
                0));
        addPanel.add(angleRotation, new GridBagConstraints(
                1,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2),
                0,
                0));     
        Dimension finalSize = new Dimension();
        Dimension initialSizePanel = new Dimension();
        initialSizePanel = addPanel.getPreferredSize();
        
        if(initialSize==null){
        	initialSize = toolbox.getSize();
        }
        
		toolbox.getContentPane().add(addPanel,BorderLayout.SOUTH);
		
		finalSize.height = initialSizePanel.height + initialSize.height;
		if(initialSizePanel.width > initialSize.width){
			finalSize.width = initialSizePanel.width;
		}
		else{
			finalSize.width = initialSize.width;
		}
        
		toolbox.setSize(finalSize);
		toolbox.repaint();			
        
    }
    
    private Collection selectItems(Geometry circle){
		int count = 0;
		Collection features = new ArrayList();
		List layers = getPanel().getLayerManager().getLayers();
		
		for (Iterator i = layers.iterator();i.hasNext();) {
			
			
			Layer actualLayer = (Layer)i.next(); 
			FeatureCollection fc = actualLayer.getFeatureCollectionWrapper().getWrappee();
			
			for (Iterator iter = fc.iterator(); iter.hasNext();) {
				Feature element = (Feature) iter.next();
				if(!circle.disjoint(element.getGeometry())){
					features.add(element);
					count++;
				}
			}
					
		}		
	    return  features;
		
    }
    
    private Coordinate getNearestPoint(Geometry box, Coordinate point){
	   	Coordinate nearestp = null;
	   	double minDist=0, dist = 0;
	   	Coordinate[] coords = box.getCoordinates();
	   	boolean first = true;
	   	for(int i=0; i < coords.length; i++){
	   		dist = GeoUtils.distance(coords[i], point); 
	   		if(first==true){
	   			minDist = dist;
	   			nearestp = (Coordinate)coords[i].clone();
	   			first=false;
	   		}	   		
	   		if (dist < minDist){
	   			minDist = dist;
	   			nearestp = (Coordinate)coords[i].clone();
	   		}
	   	}
	   	return nearestp;
	   }
    
    public void mouseReleased(MouseEvent e) {
    	try {
    		  		
	        super.mouseReleased(e);
	        
	        if (!checkLayersSelectedIsEditable("RotateSelectedItemsTool")){
	            return;
	        }
	        
	    	if(!checkAtLeastNItemsMustBeSelected(1,"RotateSelectedItemsTool")){
	            return;
	        }
	    	
	        this.centerCoord=null;   		
    		JTextField angleRotation = (JTextField) getComponentPanel(getAddPanel(toolbox,addPanel),"anglerotation");
            angleRotation.setText("0,0");
            
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }
    

    public void mouseDragged(MouseEvent e){
    	    
    	if (!checkLayersSelectedIsEditable("RotateSelectedItemsTool")){
            return;
        }
        
    	if(!checkAtLeastNItemsMustBeSelected(1,"RotateSelectedItemsTool")){
            return;
        }
    	
    	
    	if(centerCoord!=null){
	    	super.mouseDragged(e);
	        try {    	
	        		        	
	        	this.rotating = true;	      
		    	
	        }
	        catch (Throwable t) {
	        	getPanel().getContext().handleThrowable(t);
	        }
    	}
    }
    
    protected void clickReturn(double angle) throws Exception {
		
		try {
						        
            boolean dragComplete = isShapeOnScreen();
            
            if (dragComplete) {
            	
            	this.fullAngle = -Math.toRadians(angle);
            	Graphics2D g = (Graphics2D) getPanel().getGraphics();
        		drawShapeXOR(null,g);
        		fireGestureFinished();
        		
                JTextField angleRotation = (JTextField) getComponentPanel(getAddPanel(toolbox,addPanel),"anglerotation");
                angleRotation.setText("0,0");
                
            }
            
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
				
	}

	protected void angleRotationChanged(double angle, MouseEvent e) {
		
		this.anglePanel = angle;
	    	
	        try {    	
	        		        	
	        	if (!checkLayersSelectedIsEditable("RotateSelectedItemsTool")){
	                return;
	            }
	           
	        	if(!checkAtLeastNItemsMustBeSelected(1,"RotateSelectedItemsTool")){
	                return;
	            }
	            
	            this.rotating = true;
	            
	            setViewSource(e.getPoint());
	            setViewDestination(e.getPoint());
	            selectedFeatureShape = createSelectedItemsShape();
	            	            
	            redrawShape();	   
	            anglePanel = 0;
		    	
	        }
	        catch (Throwable t) {
	        	getPanel().getContext().handleThrowable(t);
	        }
    	
	}

	public double readAngleRotation() throws NoninvertibleTransformException{
		
		JTextField angleRotation = (JTextField) getComponentPanel(getAddPanel(toolbox,addPanel),"anglerotation");
				  
		double angle;
		if((angleRotation.getText().length()>0)&&(!angleRotation.getText().equals(","))&&(!angleRotation.getText().equals("-"))){			
				angle = Double.parseDouble(angleRotation.getText().replace(',', '.'));
				if(angle>360){
					angle=360;
				}
				if(angle<-360){
					angle = -360;
				}
		}
		else{
			angle=0;
		}	
		return -angle;				
	}
    
}
