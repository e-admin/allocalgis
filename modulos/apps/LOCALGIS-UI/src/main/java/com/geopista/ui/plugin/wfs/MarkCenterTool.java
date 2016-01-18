/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * JUMP is Copyright (C) 2003 Vivid Solutions
 *
 * This program implements extensions to JUMP and is
 * Copyright (C) 2004 Integrated Systems Analysts, Inc.
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
 * Stefan Steiniger
 * perriger@gmx.de
 *
 */
/*****************************************************
 * created:  		11.12.2005
 * last modified:  	28.01.2006 cursor change on mouse over corner
 * 
 * author: sstein
 * 
 * description:
 *  - scales selected items (using the bounding box) 
 * 
 *****************************************************/
package com.geopista.ui.plugin.wfs;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.ui.cursortool.scaleselecteditemstool.images.IconLoader;
import com.geopista.ui.geoutils.GeoUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.snap.SnapManager;


public class MarkCenterTool extends CenterTool {
	
    private String scaleSelectedItems = "Scale Selected Items"; 
    private String sScaleFactor = "scale factor"; 
    private EnableCheckFactory checkFactory;    
    private Geometry buffer=null;    
    private Shape outlineItemsShape=null;  
    private double xscale = 0.0;
    private double yscale = 0.0;
    private Coordinate mousePos = null;
    private Coordinate center = null;    
    DecimalFormat df2 = new DecimalFormat("##0.0#");
    boolean startScaling = false;
    double toleranceFactor = 0.2;
    private BasicStroke originalStroke = null;

    boolean somethingChanged = false;
    int style = 1;	
    Cursor cursor = null;
    WorkbenchContext context = null;
        
    public MarkCenterTool(EnableCheckFactory checkFactory) {
            	    	
    	this.checkFactory = checkFactory;        
        this.originalStroke =  new BasicStroke(
                1,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL,
                0,
                new float[] { 3, 3 },
                0);        
        setStroke(this.originalStroke);
        this.style=1;
        allowSnapping();    	
        this.cursor = this.getCursor();
        
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.cursortool.scaleselecteditemstool.languages.ScaleSelectedItemsTooli18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("ScaleSelectedItemsTool",bundle2);
    }
    public MarkCenterTool(WorkbenchContext context){
    	this.context = context;
    	((LayerViewPanel)this.context.getLayerViewPanel()).addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                deactivate();
            }
        });
    	
    }
    protected void gestureFinished() throws java.lang.Exception { 	
    	
    	if ((this.startScaling == true)&& (scaling == true)){
	        reportNothingToUndoYet();
	        ArrayList transactions = new ArrayList();
	        for (Iterator i = getPanel().getSelectionManager().getLayersWithSelectedItems().iterator();
	            i.hasNext();
	            ) {
	            Layer layerWithSelectedItems = (Layer) i.next();
	            transactions.add(createTransaction(layerWithSelectedItems));
	        }
	        EditTransaction.commit(transactions);
	        this.startScaling=false;
	        
	    	this.startScaling = false;
	    	somethingChanged = true;
	    	center = null;
	    	buffer = null;
	    	scaling = false;
	    	initMousePos = null;
	    	Collection selectedGeos = (getPanel().getSelectionManager().getSelectedItems());             
	        this.geo = (Geometry) selectedGeos.iterator().next();
	    	this.outlineItemsShape = getPanel().getJava2DConverter().toShape(this.geo);
		    this.redrawShape();
    	}
    }
    
    private EditTransaction createTransaction(Layer layer) {
        EditTransaction transaction =
            EditTransaction.createTransactionOnSelection(new EditTransaction.SelectionEditor() {
            public Geometry edit(Geometry geometryWithSelectedItems, Collection selectedItems) {
                for (Iterator j = selectedItems.iterator(); j.hasNext();) {
                    Geometry item = (Geometry) j.next();
                    scale(item);
                }
                return geometryWithSelectedItems;
            }
        }, getPanel(), getPanel().getContext(), getName(), layer, isRollingBackInvalidEdits(), false);
        return transaction;
    }  

    private void scale(Geometry geometry) {
        geometry.apply(new CoordinateFilter() {
            public void filter(Coordinate coordinate) {            	
            	
            	coordinate.x=center.x+(xscale)*(coordinate.x-center.x);            	
            	coordinate.y=center.y+(yscale)*(coordinate.y-center.y);
    
              }
        });
    }
    
    public Icon getIcon() {
    	return IconLoader.icon("ScalePolygon.gif");       
    }

    public String getName() {
        return I18N.get("ScaleSelectedItemsTool","ScaleSelectedItems");
    }

    private Geometry geo = null;
    
    public void activate(ILayerViewPanel panel){
    	super.activate((LayerViewPanel)panel);
    	
    	//Añadir el panel con el factor de escalado al toolbox
    	toolbox = getPanelToolBox();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
        JLabel factor = new JLabel(I18N.get("ScaleSelectedItemsTool","Scale"));
        final JTextField factorScale = new JTextField(8);
        factorScale.setName("factorscale");
        factorScale.setText("1,0");
        
        addPanel = new JPanel();
        addPanel.setLayout(gridBagLayout);
                        
        factorScale.addKeyListener(new KeyAdapter()
        {
           public void keyTyped(KeyEvent e)
           {
              char caracter = e.getKeyChar();	              
              if(((caracter < '0') || (caracter > '9')) &&
                      (caracter != KeyEvent.VK_BACK_SPACE) && (caracter != ','))
              {
            	  e.consume();               
              }        	     
           }
           
           	           
         });
        addPanel.add(factor, new GridBagConstraints(
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
        addPanel.add(factorScale, new GridBagConstraints(
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
        
    	
    	this.scaleSelectedItems =I18N.get("ScaleSelectedItemsTool","ScaleSelectedItems");
    	this.sScaleFactor =I18N.get("ScaleSelectedItemsTool","scalefactor");    
    	
    	try{
    		
    	/*	if(!checkExactlyNItemsMustBeSelected(1,"ScaleSelectedItemsTool")){
    			return;
    		}
    		
    		if(!checkItemsSelectedIsNotEmpty("ScaleSelectedItemsTool")){
    			return;
    		}
    		
    		if(!checkLayersSelectedIsEditable("ScaleSelectedItemsTool")){
    			return;
    		}*/
    		
    		
    		this.outlineItemsShape = getPanel().getJava2DConverter().toShape(this.geo); 
            this.setStroke(this.originalStroke);
    		this.setColor(Color.RED);
            this.style=1;
    		somethingChanged = true;
    		    		
    	}
    	catch (Throwable t) {
    		getPanel().getContext().handleThrowable(t);
    	}
    }
    
    protected void factorScaleChanged(double factor) {
    	try {    	
        	
    		if(!checkExactlyNItemsMustBeSelected(1,"ScaleSelectedItemsTool")){
    			return;
    		}
    		
    		if(!checkItemsSelectedIsNotEmpty("ScaleSelectedItemsTool")){
    			return;
    		}
    		
    		if(!checkLayersSelectedIsEditable("ScaleSelectedItemsTool")){
    			return;
    		}
	        
        	this.scaling = true;	        	
        		    	
	    	this.xscale = factor;		        
	    	this.yscale = factor;
	    	
	    	if((this.geo.getClass()==Point.class)||((this.geo.getClass()==Polygon.class)&&(this.geo.getArea()==0)))return;
	    	
	    	if(this.center==null){
	    		this.center = this.geo.getCentroid().getCoordinate();	    			
	    	}
	    	
	    	Collection selectedGeos = (getPanel().getSelectionManager().getSelectedItems());             
	        this.geo = (Geometry) selectedGeos.iterator().next();
	    		    		    	
	        this.outlineItemsShape = null;
	        Geometry geoms = (Geometry)this.geo.clone();
	        this.scale(geoms);
	        if((this.center!=null)&&(buffer!=null)){
    			geoms = geoms.union(this.buffer);
    		}
	        this.outlineItemsShape = getPanel().getJava2DConverter().toShape(geoms);
	        this.redrawShape(); 
	        
	        getPanel().getContext().setStatusMessage(I18N.get("ScaleSelectedItemsTool","ReturnAdd"));
        }
        catch (Throwable t) {
        	getPanel().getContext().handleThrowable(t);
        }
		
	}

	protected void clickReturn(double factor) throws Exception {
		
		try {
			
			if(!checkExactlyNItemsMustBeSelected(1,"ScaleSelectedItemsTool")){
    			return;
    		}
    		
    		if(!checkItemsSelectedIsNotEmpty("ScaleSelectedItemsTool")){
    			return;
    		}
    		
    		if(!checkLayersSelectedIsEditable("ScaleSelectedItemsTool")){
    			return;
    		}
        
            boolean dragComplete = isShapeOnScreen();

            if (dragComplete) {
            	
            	this.xscale = factor;
        		this.yscale = factor;
        		this.startScaling = true;
        		this.scaling = true;
        		
        		if((this.geo.getClass()==Point.class)||((this.geo.getClass()==Polygon.class)&&(this.geo.getArea()==0)))return;
        		
        		if(this.center==null){
            		this.center = this.geo.getCentroid().getCoordinate();	    			
            	}        	
        		fireGestureFinished();
        		this.xscale=1;
                this.yscale=1;
                JTextField factorScale = (JTextField) getComponentPanel(getAddPanel(toolbox,addPanel),"factorscale");
                factorScale.setText("1,0");
            }
            
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
				
	}

	public void mouseClicked(MouseEvent e) {
    	
    	try{
    		
    		if(!checkExactlyNItemsMustBeSelected(1,"ScaleSelectedItemsTool")){
    			return;
    		}
    		
    		if(!checkItemsSelectedIsNotEmpty("ScaleSelectedItemsTool")){
    			return;
    		}
    		
    		if(!checkLayersSelectedIsEditable("ScaleSelectedItemsTool")){
    			return;
    		}
	        
    		this.setMousePos(getPanel().getViewport().toModelCoordinate(e.getPoint())); //-- includes snap
   		
    		double tolerance = SnapManager.getToleranceInPixels(this.getPanel().getBlackboard()) / this.getPanel().getViewport().getScale();
    		
    		Geometry ls = geo;    		   		
    		MultiPoint mps = new GeometryFactory().createMultiPoint(ls.getCoordinates());
    		Geometry buffergeom = mps.buffer(tolerance*this.toleranceFactor);
    		Point mousep = new GeometryFactory().createPoint(this.mousePos);
    		    		
        	if (buffergeom.contains(mousep)){
        		//Obtener el punto del item seleccionado más próximo
        		this.center = this.getNearestPoint(this.geo, this.mousePos);
        		
        		Point p = new GeometryFactory().createPoint(this.center);	    	   	
	    	   	this.buffer = p.buffer(1*tolerance);   
	    	   	
	    	   	Geometry geoms = (Geometry)this.geo.clone();
	    	   	geoms = geoms.union(this.buffer);
    		    this.outlineItemsShape = getPanel().getJava2DConverter().toShape(geoms);
    		    this.redrawShape(); 
    		    
    		    this.style = 2;
	    		
        	}        	
        	        	
    	}
        catch (Throwable t) {
        	getPanel().getContext().handleThrowable(t);
        }
    }
    
    public void mouseReleased(MouseEvent e) {
    	try {
    		
    		if(!checkExactlyNItemsMustBeSelected(1,"ScaleSelectedItemsTool")){
    			return;
    		}
    		
    		if(!checkItemsSelectedIsNotEmpty("ScaleSelectedItemsTool")){
    			return;
    		}
    		
    		if(!checkLayersSelectedIsEditable("ScaleSelectedItemsTool")){
    			return;
    		}
	        
            super.mouseReleased(e);  
            this.xscale=0;
            this.yscale=0;
            JTextField factorScale = (JTextField) getComponentPanel(getAddPanel(toolbox,addPanel),"factorscale");
            factorScale.setText("1,0");
             
            
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }
   
    public void mousePressed(MouseEvent e) {
    	
        this.setStroke(this.originalStroke);
		this.setColor(Color.RED);
        this.style=2;
       
    	try{
    		this.setMousePos(getPanel().getViewport().toModelCoordinate(e.getPoint())); 
    	} 
    	catch (Throwable t) {
    		getPanel().getContext().handleThrowable(t);
    	}    	
    		
	    try {
	    	
	    	if(!checkExactlyNItemsMustBeSelected(1,"ScaleSelectedItemsTool")){
    			return;
    		}
    		
    		if(!checkItemsSelectedIsNotEmpty("ScaleSelectedItemsTool")){
    			return;
    		}
    		
    		if(!checkLayersSelectedIsEditable("ScaleSelectedItemsTool")){
    			return;
    		}
	    		                   
	        super.mousePressed(e);
	            
	        Collection selectedGeos = (getPanel().getSelectionManager().getSelectedItems());             
	        this.geo = (Geometry) selectedGeos.iterator().next();
	            
	        this.setInitialMousePos(getPanel().getViewport().toModelCoordinate(e.getPoint())); 
	         
	        this.outlineItemsShape = getPanel().getJava2DConverter().toShape(this.geo);
	    	this.redrawShape(); 
	        	    	
	    	if((this.geo.getClass()==Point.class)||((this.geo.getClass()==Polygon.class)&&(this.geo.getArea()==0)))return;
	    	
	    	if(this.center==null){
	    		this.center = this.geo.getCentroid().getCoordinate();	    			
	    	}
	    	
	    	this.setInitialDist();
	    	
	        this.startScaling = true;	
	        somethingChanged = true;
	        
	    } catch (Throwable t) {
	            getPanel().getContext().handleThrowable(t);
	    }
    	
    }    
    
    private boolean scaling = false;

    public void mouseDragged(MouseEvent e){
    	
    	if(!checkExactlyNItemsMustBeSelected(1,"ScaleSelectedItemsTool")){
			return;
		}
		
		if(!checkItemsSelectedIsNotEmpty("ScaleSelectedItemsTool")){
			return;
		}
		
		if(!checkLayersSelectedIsEditable("ScaleSelectedItemsTool")){
			return;
		}
        
    	if (startScaling == true){
	    	super.mouseDragged(e);
	        try {    	
	        	
	        	this.scaling = true;	        	
	        	
		    	this.setMousePos(getPanel().getViewport().toModelCoordinate(e.getPoint())); //-- includes snap
		    			    	
		    	this.xscale = GeoUtils.distance(this.mousePos, this.center)/this.initialDist;
		    	this.yscale = GeoUtils.distance(this.mousePos, this.center)/this.initialDist;
		    			    		
		    	this.yscale=this.xscale;
		    	
		    	JPanel panel = getAddPanel(toolbox,addPanel);
		    	JTextField factorScale = (JTextField)getComponentPanel(panel,"factorscale");
		    	String messageFactor = df2.format(Math.abs(xscale));
		    	factorScale.setText(messageFactor);
		    	
		        this.outlineItemsShape = null;
		        Geometry geoms = (Geometry)this.geo.clone();
		        this.scale(geoms);
		        if((this.center!=null)&&(buffer!=null)){
	    			geoms = geoms.union(this.buffer);
	    		}
		        this.outlineItemsShape = getPanel().getJava2DConverter().toShape(geoms);
		        somethingChanged = true;
	        }
	        catch (Throwable t) {
	        	getPanel().getContext().handleThrowable(t);
	        }
    	}
    }
   
    public Cursor getCursor() {
        return Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    }
    
    /*public Cursor getCursor() {
    	return Toolkit.getDefaultToolkit().createCustomCursor(
                IconLoader.icon("ScalePolygon.gif").getImage(),
                new java.awt.Point(16, 16),
                getName());    
    }*/
    
    
    public void mouseMoved(MouseEvent e){
    	try{
    		    		
    		if(!checkExactlyNItemsMustBeSelected(1,"ScaleSelectedItemsTool")){
    			return;
    		}
    		
    		if(!checkItemsSelectedIsNotEmpty("ScaleSelectedItemsTool")){
    			return;
    		}
    		
    		if(!checkLayersSelectedIsEditable("ScaleSelectedItemsTool")){
    			return;
    		}
    		        
    		this.setMousePos(getPanel().getViewport().toModelCoordinate(e.getPoint())); //-- includes snap
    		
        	if ((this.style == 2)){
	                this.setStroke(this.originalStroke);
	                this.getPanel().setCursor(this.cursor);	   
			        this.somethingChanged = true;			        
        	}
        	
        	if(somethingChanged == true){
    			this.redrawShape(); 	
    			somethingChanged = false;
    		}
    	}
        catch (Throwable t) {
        	getPanel().getContext().handleThrowable(t);
        }
    }    
    
   
    protected Shape getShape(){
    	return this.outlineItemsShape;			
    }
    
    public void deactivate(MouseEvent e){
    	this.cleanup((Graphics2D)getPanel().getGraphics());
    }
    
    protected void setMousePos(Coordinate destination) {
        this.mousePos = snap(destination);
   }   
    
    private double initialDist = 0;
    
    private Coordinate initMousePos = null;
    protected void setInitialMousePos(Coordinate destination) {
        this.initMousePos = snap(destination);
   }   
    
   protected void setInitialDist(){
	   if((initMousePos!=null)&&(center!=null)){
		   this.initialDist = GeoUtils.distance(initMousePos, center);
	   }
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
   
   public void cancelGesture() {
       
       super.cancelGesture();      
       center = null;
       buffer = null;
       initMousePos = null;
       removePanel(toolbox,addPanel,initialSize);       
       getPanel().getContext().setStatusMessage("");
       
   }
     
   public double readFactorScale() throws NoninvertibleTransformException{
		
		JTextField factorScale = (JTextField) getComponentPanel(getAddPanel(toolbox,addPanel),"factorscale");
				  
		double factor;
		if((factorScale.getText().length()>0)&&(!factorScale.getText().equals(","))){			
				factor = Double.parseDouble(factorScale.getText().replace(',', '.'));			
		}
		else{
			factor=1;
		}	
		return factor;				
	}
   
   
   public void paintCenter(double x, double y) throws Exception{
	   	Coordinate coord = new Coordinate(x,y);
        GeometryFactory h = new GeometryFactory();
	   	Geometry geoms = h.createPoint(coord);
	    this.originalStroke = new BasicStroke(
	            1,
	            BasicStroke.CAP_BUTT,
	            BasicStroke.JOIN_BEVEL,
	            0,
	            new float[] { 5.0f, 5.0f },
	            0);   
	    setStroke(originalStroke);
	   	double scale = context.getLayerViewPanel().getViewport().getScale();
	   	Geometry buffer = geoms.buffer(28.5/scale);   
	   	geoms = geoms.union(buffer);
	   	this.geo = geoms;
		this.activate(this.context.getLayerViewPanel());
		this.redrawShape();
   }
}
