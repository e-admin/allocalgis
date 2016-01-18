
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
 *
 * Integrated Systems Analysts, Inc.
 * 630C Anchors St., Suite 101
 * Fort Walton Beach, Florida
 * USA
 *
 * (850)862-7321
 */

package com.geopista.ui.cursortool.drawconstrainedarc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.geopista.ui.cursortool.ConstrainedMultiClickArcTool;
import com.geopista.ui.cursortool.drawconstrainedarc.images.IconLoader;
import com.geopista.ui.cursortool.editing.FeatureDrawingUtil;
import com.geopista.ui.geoutils.Arc;
import com.geopista.ui.geoutils.GeoUtils;
import com.geopista.ui.geoutils.MathVector;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.model.UndoableEditReceiver;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class DrawConstrainedArcTool extends ConstrainedMultiClickArcTool {
    private FeatureDrawingUtil featureDrawingUtil;
    
    protected ToolboxDialog toolbox = null;    
	protected JPanel addPanel = null;
	protected Dimension initialSize = null;
		
    private double radio = 0;
    private double angulo = 0;
    
    private DrawConstrainedArcTool(FeatureDrawingUtil featureDrawingUtil) {
        drawClosed = false;
        this.featureDrawingUtil = featureDrawingUtil;
        
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.cursortool.drawconstrainedarc.languages.DrawConstrainedArcTooli18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("DrawConstrainedArcTool",bundle2);
        
    }

    public static CursorTool create(LayerNamePanelProxy layerNamePanelProxy) {
        FeatureDrawingUtil featureDrawingUtil = new FeatureDrawingUtil(layerNamePanelProxy);

        return featureDrawingUtil.prepare(new DrawConstrainedArcTool(
                featureDrawingUtil), true);
    }

    public String getName() {
        return I18N.get("DrawConstrainedArcTool","Draw-Constrained-Arc");
    }

    public Icon getIcon() {
    	return IconLoader.icon("DrawArcConstrained.gif");
    }

    protected void gestureFinished() throws Exception {
    	
        reportNothingToUndoYet();

        if (!checkArc()) {
            return;
        }

        execute(featureDrawingUtil.createAddCommand(getArc(),
                isRollingBackInvalidEdits(), getPanel(), this));
    }

    protected LineString getArc() throws NoninvertibleTransformException
    {       
        ArrayList points = new ArrayList(getCoordinates());
        
        if (points.size() > 1)
        {
            Coordinate a = (Coordinate) points.get(0);
            Coordinate b = (Coordinate) points.get(1);
            Coordinate c = tentativeCoordinate;
            
            if (points.size() > 2)
            {
                c = (Coordinate) points.get(points.size() - 1);
            }

            MathVector v1 = (new MathVector(b)).vectorBetween(new MathVector(a));
            MathVector v2 = (new MathVector(c)).vectorBetween(new MathVector(a));
            double arcAngle = v1.angleDeg(v2);    
            
            
            /*
            boolean cwQuad = ((fullAngle >= 0.0) &&(fullAngle <= 90.0) && clockwise);
            boolean ccwQuad = ((fullAngle < 0.0) &&(fullAngle >= -90.0) && !clockwise);
            
            
            if ((fullAngle > 90.0) || (fullAngle < -90))
            {
                if ((clockwise ) || (!clockwise ))
                    fullAngle = 360 - arcAngle;
                else
                   fullAngle = arcAngle; 
            }
            else
            {
                fullAngle = arcAngle;
            }

            if (!clockwise)
                fullAngle = -fullAngle;*/
                
              
            boolean toRight = new GeoUtils().pointToRight(tentativeCoordinate, a, b);
            
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

            if (!clockwise)
                fullAngle = -fullAngle;
            
            if(fullAngle!=0 && fullAngle!=360){

            	Arc arc = new Arc(a, b, fullAngle);
            	return arc.getLineString();
            	            	
            }            
            
        }
        return null;
    }

    protected boolean checkArc() throws NoninvertibleTransformException {
        if (getCoordinates().size() < 3) {
            getPanel().getContext().warnUser(I18N.get("DrawConstrainedArcTool","The-arc-must-have-at-least-3-points"));

            return false;
        }

        IsValidOp isValidOp = new IsValidOp(getArc());

        if (!isValidOp.isValid()) {
            getPanel().getContext().warnUser(isValidOp.getValidationError()
                                                      .getMessage());

            if (getWorkbench().getBlackboard().get(EditTransaction.ROLLING_BACK_INVALID_EDITS_KEY, false)) {
                return false;
            }
        }

        return true;
    }
    
    public void activate(ILayerViewPanel panel){
    	
    	super.activate(panel);
	//  Añadir el panel con el factor de escalado al toolbox
		toolbox = getPanelToolBox();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
	    /*JLabel labelRadio = new JLabel(I18N.get("DrawConstrainedArcTool","length"));
	    final JTextField textRadio = new JTextField(8);
	    textRadio.setName("radio");
	    textRadio.setText("0,0");*/
	    
	    JLabel labelAngulo = new JLabel(I18N.get("DrawConstrainedArcTool","angle"));
	    final JTextField textAngulo = new JTextField(8);
	    textAngulo.setName("angulo");
	    textAngulo.setText("0,0");
	    
	    addPanel = new JPanel();
	    addPanel.setLayout(gridBagLayout);
	                    
	    /*textRadio.addKeyListener(new KeyAdapter()
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
	    			  
	    			   DrawConstrainedArcTool.this.radio = readRadio();        			  
	    			   DrawConstrainedArcTool.this.clickReturn();
			          
		           }catch(Throwable t){
	            		  getPanel().getContext().handleThrowable(t);
	    		   }
	            }	       
	    	   
	       }
	       	           
	       public void keyReleased(KeyEvent e)
	       {	        	  
				   try{	 	    	
					   
					   DrawConstrainedArcTool.this.radio = readRadio();	        			  
	    			  //DrawConstrainedArcTool.this.factorScaleChanged(factorScale);	
	    			  
		           }catch(Throwable t){
	            		  getPanel().getContext().handleThrowable(t);
	    		   }	    		      	   
	       }	           
	    });*/
	    
	    textAngulo.addKeyListener(new KeyAdapter()
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
	    			  
	    			   DrawConstrainedArcTool.this.angulo = readAngulo(); 
	    			   if((DrawConstrainedArcTool.this.angulo!=0)&&(DrawConstrainedArcTool.this.angulo!=360)){
	    				   DrawConstrainedArcTool.this.clickReturn();
	    				   coordinates.clear();
	    			   }	    			   
			          
		           }catch(Throwable t){
	            		  getPanel().getContext().handleThrowable(t);
	    		   }
	            }	       
	    	   
	       }
	       	           
	       public void keyReleased(KeyEvent e)
	       {	        	  
				   try{	 	    	
					   
					   DrawConstrainedArcTool.this.angulo = readAngulo();	 					   
			           DrawConstrainedArcTool.this.keyReleased();
	    			  //DrawConstrainedArcTool.this.factorScaleChanged(factorScale);	
	    			  
		           }catch(Throwable t){
	            		  getPanel().getContext().handleThrowable(t);
	    		   }	    		      	   
	       }	           
	    });
	    
	    /*addPanel.add(labelRadio, new GridBagConstraints(
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
	    addPanel.add(textRadio, new GridBagConstraints(
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
	            0)); */    
	    addPanel.add(labelAngulo, new GridBagConstraints(
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
	    addPanel.add(textAngulo, new GridBagConstraints(
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
    
    protected void clickReturn() {
		
		try {
			
			if((coordinates.size()>1)){
				
				Coordinate center = (Coordinate)coordinates.get(0);
				Coordinate firstPoint = (Coordinate)coordinates.get(1);
				radio = center.distance(firstPoint);			
				MathVector v1 = (new MathVector(firstPoint)).vectorBetween(new MathVector(center));            
				MathVector v2 = v1.rotateDeg(angulo);
				tentativeCoordinate.x = center.x + v2.x();
				tentativeCoordinate.y = center.y + v2.y();
				
				fullAngle = angulo;
				
				if(angulo>0)
					clockwise = true;
				else
					clockwise=false;
				
				
				super.add(tentativeCoordinate);
				redrawShape();

				fireGestureFinished();
				
			}
			
		} catch (Throwable t)
        {
            getPanel().getContext().handleThrowable(t);
        }
	}

   
	public double readAngulo() throws NoninvertibleTransformException{
		
		JTextField textAngulo = (JTextField) getComponentPanel(getAddPanel(toolbox,addPanel),"angulo");
				  
		double angulo;
		if((textAngulo.getText().length()>0)&&(!textAngulo.getText().equals(","))&&(!textAngulo.getText().equals("-"))){			
				angulo = Double.parseDouble(textAngulo.getText().replace(',', '.'));			
		}
		else{
			angulo=0;
		}	
		return angulo;				
	}
	
	public void cancelGesture() {
		try{
			coordinates.clear();
			redrawShape();
			removePanel(toolbox,addPanel,initialSize);

		} catch (Throwable t)
		{
			getPanel().getContext().handleThrowable(t);
		}
	}
	 
	 protected void keyReleased() {
			
			try {
				if(coordinates.size()>1){
					
					Coordinate center = (Coordinate)coordinates.get(0);
					Coordinate firstPoint = (Coordinate)coordinates.get(1);
					radio = center.distance(firstPoint);			
					MathVector v1 = (new MathVector(firstPoint)).vectorBetween(new MathVector(center));            
					MathVector v2 = v1.rotateDeg(angulo);
					tentativeCoordinate.x = center.x + v2.x();
					tentativeCoordinate.y = center.y + v2.y();
					if(angulo>0)
						clockwise = true;
					else
						clockwise = false;
					super.add(tentativeCoordinate);
					redrawShape();
					
				}
				
			} catch (Throwable t)
	        {
	            getPanel().getContext().handleThrowable(t);
	        }
		}
}
