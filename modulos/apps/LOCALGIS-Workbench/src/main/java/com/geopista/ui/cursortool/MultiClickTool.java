
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

package com.geopista.ui.cursortool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.editor.GeopistaEditor;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

/**
 *  A VisualIndicatorTool that allows the user to draw shapes with multiple
 *  vertices. Double-clicking ends the gesture.
 */
public abstract class MultiClickTool extends AbstractCursorTool {

//24.iii.03 Dropped drag handling because it's too easy to do a micro-drag when
//we mean a click. [Jon Aquino]

    private List coordinates = new ArrayList();
    private Coordinate tentativeCoordinate;
    
	private boolean isSelectedRel = false;
	
	private static AppContext appContext = (AppContext) AppContext
    .getApplicationContext();
	
    public MultiClickTool() {
    	
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.cursortool.languages.MultiClickTooli18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("MultiClickTool",bundle2);
    }

    /**
     * Will return an empty List once the shape is cleared.
     * @see MultiClickTool#clearShape
     */
    public List getCoordinates() {
        return Collections.unmodifiableList(coordinates);
    }

    public void cancelGesture() {
        //It's important to clear the data when #cancelGesture is called.
        //Otherwise, you get behaviour like the following:
        //  --  Combine a DragTool with a MultiClickTool using OrCompositeTool
        //  --  Drag a box. A box appears. Release the mouse.
        //  --  Move the mouse. You see a rubber band from MultiClickTool because
        //      the points haven't been cleared. [Jon Aquino]
    	//this.deactivate();
    	super.cancelGesture();        
       	coordinates.clear();           
        isSelectedRel = ((JCheckBox)getComponentPanel(addPanel, "RelCoord")).isSelected();
        removePanel(toolbox,addPanel,initialSize);        
        getPanel().getContext().setStatusMessage("");
        
        if (getPanel().getContext() instanceof GeopistaEditor){
			((GeopistaEditor)getPanel().getContext()).setTimeMessage("");
		}
		else if (getPanel().getContext() instanceof WorkbenchGuiComponent){
			((WorkbenchGuiComponent)getPanel().getContext()).setTimeMessage("");
		}
    }

    public void mouseReleased(MouseEvent e) {
    	
        try {
        	
        	//Para que no se borren los puntos introducidos a través del panel 
        	//al pulsar por primera vez con el ratón 
        	//numberClicks++;  
        	
            //Can't assert that coordinates is not empty at this point because
            //of the following situation: NClickTool, n=1, user double-clicks.
            //Two events are generated: clickCount=1 and clickCount=2. 
            //When #mouseReleased is called with the clickCount=1 event,
            //coordinates is not empty. But then #finishGesture is called and the
            //coordinates are cleared. When #mouseReleased is then called with
            //the clickCount=2 event, coordinates is empty! [Jon Aquino]

            //Even though drawing is done in #mouseLocationChanged, call it here
            //also so that #isGestureInProgress returns true on a mouse click.
            //This is mainly for the benefit of OrCompositeTool, which
            //calls #isGestureInProgress. [Jon Aquino]
            //Can't do this in #mouseClicked because #finishGesture may be called
            //by #mouseReleased (below), which happens before #mouseClicked,
            //resulting in an IndexOutOfBoundsException in #redrawShape. [Jon Aquino]
            if (e.getClickCount() == 1) {
                //A double-click will generate two events: one with click-count = 1 and
                //another with click-count = 2. Handle the click-count = 1 event and
                //ignore the rest. Otherwise, the following problem can occur:
                //  --  A click-count = 1 event is generated; #redrawShape is called
                //  --  #isFinishingClick returns true; #finishGesture is called
                //  --  #finishGesture clears the points
                //  --  A click-count = 2 event is generated; #redrawShape is called.
                //      An IndexOutOfBoundsException is thrown because points is empty.
                //[Jon Aquino]
                tentativeCoordinate = snap(e.getPoint());
                redrawShape();
            }

            super.mouseReleased(e);

            //Check for finish at #mouseReleased rather than #mouseClicked.
            //#mouseReleased is a more general condition, as it applies to both
            //drags and clicks. [Jon Aquino]
            if (isFinishingRelease(e)) {
                finishGesture();
            }
        } catch (Throwable t) {
        	if ((t.getMessage()!=null) && (!t.getMessage().contains("Unable to Stroke shape"))){
	        	t.printStackTrace();
	            getPanel().getContext().handleThrowable(t);
        	}
        }
    }
    
    protected void mouseLocationChanged(Point2D e) {
//    	Si estamos pintando un polígono o una linea
        // Mostramos la longitud y el area del mismo
        try {
			    ArrayList currentCoordinates = new ArrayList(getCoordinates());
			    currentCoordinates.add(getPanel().getViewport().toModelCoordinate(e));
			    if (isShapeOnScreen()){
			    	display(currentCoordinates, getPanel());
			    }

        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
		}
    	
    	try {
         if (coordinates.isEmpty()) {
                return;
            }
            tentativeCoordinate = snap(e);
            redrawShape();
            
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }
    
    protected void mouseLocationChanged(MouseEvent e) {
        try {
            if (coordinates.isEmpty()) {
                return;
            }

            tentativeCoordinate = snap(e.getPoint());
            redrawShape();
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }

    public void mouseMoved(MouseEvent e) {  
    	updatePanelCoords(e.getPoint());
        mouseLocationChanged(e.getPoint());
    }
        
    public void mouseDragged(MouseEvent e) {    	
    	updatePanelCoords(e.getPoint());
        mouseLocationChanged(e.getPoint());
    }
        
    protected void add(Coordinate c) {
        coordinates.add(c);
    }
   
    public void mousePressed(MouseEvent e) {
        try {
        	
        	
            super.mousePressed(e);
            Assert.isTrue(e.getClickCount() > 0);
                        
            //Don't add more than one point for double-clicks. A double-click will
            //generate two events: one with click-count = 1 and another with
            //click-count = 2. Handle the click-count = 1 event and ignore the rest.
            //[Jon Aquino]
            if (e.getClickCount() != 1) {
                return;
            }

            add(snap(e.getPoint()));
            
            //Meessage for panel of coordinates
            getPanel().getContext().setStatusMessage(I18N.get("MultiClickTool","ReturnAdd"));
            
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }

    protected Shape getShape() throws NoninvertibleTransformException {
        
        GeneralPath path = new GeneralPath();
        if(!coordinates.isEmpty()){
	        Point2D firstPoint = getPanel().getViewport().toViewPoint((Coordinate)coordinates.get(0));
	        path.moveTo((float) firstPoint.getX(), (float) firstPoint.getY());
	
	        for (int i = 1; i < coordinates.size(); i++) { //start 1 [Jon Aquino]
	
	            Coordinate nextCoordinate = (Coordinate) coordinates.get(i);
	            Point2D nextPoint = getPanel().getViewport().toViewPoint(nextCoordinate);
	            path.lineTo((int) nextPoint.getX(), (int) nextPoint.getY());
	        }
	        Point2D tentativePoint = getPanel().getViewport().toViewPoint(tentativeCoordinate);
	        path.lineTo((int) tentativePoint.getX(), (int) tentativePoint.getY());
        }
        return path;
    }

    protected boolean isFinishingRelease(MouseEvent e) {
        return e.getClickCount() == 2;
    }

    protected Coordinate[] toArray(List coordinates) {
        return (Coordinate[]) coordinates.toArray(new Coordinate[] {  });
    }

    protected void finishGesture() throws Exception {
        clearShape();

        try {
            fireGestureFinished();
        } finally {
            
            coordinates.clear();
        }
    }
      
	public void activate(ILayerViewPanel layerViewPanel) {
										
			super.activate((LayerViewPanel)layerViewPanel);	
			toolbox = getPanelToolBox();
						
			GridBagLayout gridBagLayout = new GridBagLayout();
	        JLabel X = new JLabel("X");
	        final JTextField textX = new JTextField(10);
	        textX.setName("X");
	        
	        addPanel = new JPanel();
	        addPanel.setLayout(gridBagLayout);
	        JLabel Y = new JLabel("Y");
	        final JTextField textY = new JTextField(10);
	        textY.setName("Y");
	        
	        JCheckBox relativeCheckBox = new JCheckBox();
	        relativeCheckBox.setText(I18N.get("MultiClickTool","RelativeCoordinates"));
	        relativeCheckBox.setName("RelCoord");
	        relativeCheckBox.setSelected(isSelectedRel);
	        
	        if(this.getName().compareTo("Draw Point")==0){
	        	relativeCheckBox.setEnabled(false);
	        }
	        	        
	        textX.addKeyListener(new KeyAdapter()
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
	        			  
	        			   Point2D newPoint = readTextXY(e);	        			   
	        			 
	        			  MultiClickTool.this.clickReturn(newPoint);
				          
			           }catch(Throwable t){
		            		  getPanel().getContext().handleThrowable(t);
	        		   }
		            }	       
	        	   if((e.isControlDown())&&(caracter==KeyEvent.VK_ENTER)){	
	        		   try{
	        			   
	        			   MultiClickTool.this.clickCtrlReturn();
	        			   
	        		   }catch(Throwable t){
		            		  getPanel().getContext().handleThrowable(t);
	        		   }
	        	   }
	           }
	           	           
	           public void keyReleased(KeyEvent e)
	           {	        	  
	    			   try{	 	    	
	    				   
	    				  Point2D newPoint = readTextXY(e);
	        			  MultiClickTool.this.mouseLocationChanged(newPoint);	
	        			  
			           }catch(Throwable t){
		            		  getPanel().getContext().handleThrowable(t);
	        		   }	    		      	   
	           }	           
	        });
	        	          
	        textY.addKeyListener(new KeyAdapter()
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
		        			  Point2D newPoint = readTextXY(e);
		        			  MultiClickTool.this.clickReturn(newPoint);					        
				           }catch(Throwable t){
			            		  getPanel().getContext().handleThrowable(t);
		        		   }
			            }
		        	   if((e.isControlDown())&&(caracter==KeyEvent.VK_ENTER)){	
		        		   try{		        			   
		        			   MultiClickTool.this.clickCtrlReturn();		        			   
		        		   }catch(Throwable t){
			            		  getPanel().getContext().handleThrowable(t);
		        		   }
		        	   }
		           }
		           	           
		           public void keyReleased(KeyEvent e)
		           {		        	 
	        		try{	   
	        			
	        			Point2D newPoint = readTextXY(e);
	        			MultiClickTool.this.mouseLocationChanged(newPoint);		        			  
				    }catch(Throwable t){
				    	getPanel().getContext().handleThrowable(t);
		        	}	        		
		           }
	        });
	               
	        addPanel.add(X, new GridBagConstraints(
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
	        addPanel.add(textX, new GridBagConstraints(
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
	        
	        addPanel.add(Y, new GridBagConstraints(
	                0,
	                2,
	                1,
	                1,
	                0.0,
	                0.0,
	                GridBagConstraints.CENTER,
	                GridBagConstraints.HORIZONTAL,
	                new Insets(2, 2, 2, 2),
	                0,
	                0));
	        addPanel.add(textY, new GridBagConstraints(
	                1,
	                2,
	                1,
	                1,
	                0.0,
	                0.0,
	                GridBagConstraints.CENTER,
	                GridBagConstraints.HORIZONTAL,
	                new Insets(2, 2, 2, 2),
	                0,
	                0));     
	        addPanel.add(relativeCheckBox, new GridBagConstraints(
	                0,
	                3,
	                2,
	                2,
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
	
	protected void updatePanelCoords(Point2D e) {
		try {           
			
	        //Fix the coordinates in the fileld X and Y						
	        JPanel panel = getAddPanel(toolbox,addPanel);
	        
	        JTextField textFieldX = (JTextField)getComponentPanel(panel, "X");
	        JTextField textFieldY = (JTextField)getComponentPanel(panel, "Y");
	        JCheckBox relCheckBox = (JCheckBox)getComponentPanel(panel, "RelCoord");
	        if(relCheckBox.isSelected()){
	        	if(!coordinates.isEmpty()){
	        		Coordinate lastCoordinate = (Coordinate) coordinates.get(coordinates.size()-1);
	        		
	        		String messageCoordinateX = "" + getPanel().format(getPanel().getViewport().toModelCoordinate(e).x-lastCoordinate.x);	        		
		        	textFieldX.setText(messageCoordinateX);
		        	
		        	String messageCoordinateY = "" + getPanel().format(getPanel().getViewport().toModelCoordinate(e).y-lastCoordinate.y);
		        	textFieldY.setText(messageCoordinateY);
		        	
		        	//Meessage for panel of coordinates
		        	getPanel().getContext().setStatusMessage(I18N.get("MultiClickTool","ReturnAdd"));
	        	}
	        	else{
	        		//Message needed for relative coordinates
	        		getPanel().getContext().setStatusMessage(I18N.get("MultiClickTool","OnePoint"));
	        	}
	        }
	        else{
	        
	        	String messageCoordinateX = "" + getPanel().format(getPanel().getViewport().toModelCoordinate(e).x);
	        	textFieldX.setText(messageCoordinateX);
	        	        
	        	String messageCoordinateY = "" + getPanel().format(getPanel().getViewport().toModelCoordinate(e).y);
	        	textFieldY.setText(messageCoordinateY);
	        	
	        	//Meessage for panel of coordinates	            
	        	getPanel().getContext().setStatusMessage(I18N.get("MultiClickTool","ReturnAdd"));
	        }
	       
	    } catch (Throwable t) {
	        getPanel().getContext().handleThrowable(t);
	    }
	}
			
	public void clickReturn(Point2D newPoint){		
		try {
            
            add(snap(newPoint));            
            getPanel().getContext().setStatusMessage(I18N.get("MultiClickTool","ReturnAdd"));
            updatePanelCoords(newPoint);
            
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
		
	}
	
	public void clickCtrlReturn() throws Exception{
		MultiClickTool.this.finishGesture();
	}
    
	public Point2D readTextXY(KeyEvent e) throws NoninvertibleTransformException{
		
		JTextField textX = (JTextField) getComponentPanel(getAddPanel(toolbox,addPanel),"X");
		JTextField textY = (JTextField) getComponentPanel(getAddPanel(toolbox,addPanel),"Y");
		  
		double X,Y;
		if((textX.getText().length()>0)&&(!textX.getText().equals(","))&&(!textX.getText().equals("-"))){			
				X = Double.parseDouble(textX.getText().replace(',', '.'));			
		}
		else{
			X=0;
		}		
		if((textY.getText().length()>0)&&(!textY.getText().equals(","))&&(!textY.getText().equals("-"))){
			Y = Double.parseDouble(textY.getText().replace(',', '.'));
		}
		else{
			Y=0;
		}
				
		//For the relative coordinates
		JPanel panel = getAddPanel(toolbox,addPanel);               
        JCheckBox relCheckBox = (JCheckBox)getComponentPanel(panel, "RelCoord");
        if((relCheckBox.isSelected())&&(!coordinates.isEmpty())){        	
        		Coordinate firstCoordinate = (Coordinate) coordinates.get(coordinates.size()-1);
        		X = X + firstCoordinate.x;
        		Y = Y + firstCoordinate.y;     		
        }
               	
		Coordinate coordinatePoint = new Coordinate(X,Y);	        			  
		Point2D viewPoint2D = getPanel().getViewport().toViewPoint(coordinatePoint);
		               
		return viewPoint2D;
	}
	
	//Miguel
	// Copiado de GeopistaMeasueTool.java 
	
	private void display(List coordinates, LayerViewPanel panel)
	    throws NoninvertibleTransformException {
	    display(distance(coordinates), panel,getArea(coordinates));
	}

	/**
	 * @param distance
	 * @param panel
	 * @param area
	 */
	private void display(double distance, LayerViewPanel panel, double area) {
	    
	    String unitName = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_UNIT_NAME,"",true);
	    String unitEquivalence = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_UNIT_EQUIVALENCE,"",true);
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

	/**
	 * @param coordinates
	 * @return
	 */
	private double distance(List coordinates) {
	    double distance = 0;
	
	    for (int i = 1; i < coordinates.size(); i++) {
	        distance += ((Coordinate) coordinates.get(i - 1)).distance((Coordinate) coordinates.get(
	                i));
	    }
	
	    return distance;
	}

	/**
	 * @param sourceCoordinates
	 * @return
	 */
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
