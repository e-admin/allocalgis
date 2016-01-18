package com.geopista.ui.cursortool.parallelitemstool;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.ui.cursortool.AbstractCursorTool;
import com.geopista.ui.cursortool.parallelitemstool.images.IconLoader;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.SelectionManagerProxy;
import com.vividsolutions.jump.workbench.ui.ViewportListener;
import com.vividsolutions.jump.workbench.ui.EditTransaction.SelectionEditor;
import com.vividsolutions.jump.workbench.ui.snap.SnapManager;
import com.vividsolutions.jts.geom.LineString;


public class ParallelItemsTool extends AbstractCursorTool{
	
	private EnableCheckFactory checkFactory;	
    private ArrayList parallelFeatures = new ArrayList();  
    protected IAbstractSelection selection;
    private Shape outlineItemsShape=null; 
    
    private Feature referenceFeature = null;
    private Layer referenceLayer = null;
    private Coordinate mousePos = null;
    double toleranceFactor = 2.0;
    private Geometry buffer=null;        
    private double Px = 0;
    private double Py = 0;
    private Point initialCenter = null;   
    private int n = 0;
    private double distance = 0;
   
    
    private static AppContext aplicacion = (AppContext) AppContext
    .getApplicationContext();

    public ParallelItemsTool(EnableCheckFactory checkFactory) {
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
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.cursortool.parallelitemstool.languages.ParallelItemsTooli18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("ParallelItemsTool",bundle2);
    }
    
    public String getName(){
    	String name = I18N.get("ParallelItemsTool","ParallelItems");
    	return name;
    }

   protected void gestureFinished() throws java.lang.Exception {
        
	   reportNothingToUndoYet();
	   
	   selection = getPanel().getSelectionManager().getFeatureSelection();
                        
        for (Iterator i = getPanel().getSelectionManager().getLayersWithSelectedItems().iterator();
        i.hasNext();
        ) {        	
	    	final Layer layerWithSelectedItems = (Layer) i.next();
	    	
	    	if (layerWithSelectedItems.isEditable()){
	    	
	            setParallelFeatures(layerWithSelectedItems);
	    	}
        	else{        		
        		String warning = I18N.get("ParallelItemsTool","SelectedItemsLayersMustBeEditable") + " (" + layerWithSelectedItems.getName() + ")";
	            getPanel().getContext().warnUser(warning);        		
        	}
        }
              
	    boolean doExecute = false;
	    int cursedfeatures=0;
	   	            
	    for (Iterator j = parallelFeatures.iterator(); j.hasNext();) {
	    	final GeopistaFeature feature = (GeopistaFeature) j.next();
            cursedfeatures++;
            if(!((feature.getAttributes().length ==1)&&(feature.getSchema().getAttributeType(0) == AttributeType.GEOMETRY))){
            	boolean resultAttributes = GeopistaValidatePlugin.showFeatureDialog(
 		                feature, feature.getLayer());
 		        if (resultAttributes == true)
 		        {
 		        	doExecute=true;	
 		        }
 		        else{
 		        	doExecute = false;
 		        	if((parallelFeatures.size()-cursedfeatures)>0){
 		        		
 		        		if (JOptionPane.showConfirmDialog(
	 		                            (Component) getIWorkbench().getGuiComponent(),
	 		                           I18N.get("ParallelItemsTool","CancellAll"),
	 		                           I18N.get("ParallelItemsTool","Cancell"),
	 		                                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
	 		            		
 		        			break; 		            		 
	 		            	  
 		            	}
 		         	}
 		   	}
 		    else{
 		    	doExecute=true;	
 		    }
            if(doExecute){
		            execute(new UndoableCommand(getName()) {
		                public void execute() { 
		                	
		                        feature.getLayer().getFeatureCollectionWrapper().add(feature);     
		                	
		                }
		                public void unexecute() {
		                	                     
		                        feature.getLayer().getFeatureCollectionWrapper().remove(feature); 
		                        if (getPanel().getLayerManager().getUndoableEditReceiver().isAborted()){
		                        	getPanel().getLayerManager().getUndoableEditReceiver().setAborted(false);
		                        }
		                	
		                }
		            });
            }
	    }
	      	        
	    parallelFeatures.clear();	    
	    getPanel().getSelectionManager().clear();
 		selection.selectItems(referenceLayer,referenceFeature);
 		getPanel().getSelectionManager().setPanelUpdatesEnabled(true);
 		getPanel().getSelectionManager().updatePanel(); 
	 
    }
   
   private void setParallelFeatures(Layer layer) throws NoninvertibleTransformException, ClassNotFoundException, IllegalAccessException, InstantiationException {
       createParallelFeatures(new EditTransaction.SelectionEditor() {
          public Geometry edit(Geometry geometryWithSelectedItems, Collection selectedItems) {            	
              for (Iterator j = selectedItems.iterator(); j.hasNext();) {
                  Geometry item = (Geometry) j.next();                                     
                  move(item);         
              }
              return geometryWithSelectedItems;   
          }
      }, getPanel(), layer);
     
  }
   
   public void createParallelFeatures(
	       SelectionEditor editor,
	       SelectionManagerProxy selectionManagerProxy,	
	       Layer layer) throws NoninvertibleTransformException, ClassNotFoundException, IllegalAccessException, InstantiationException {
	   
	       for (Iterator i =
	           selectionManagerProxy
	               .getSelectionManager()
	               .getFeaturesWithSelectedItems(layer)
	               .iterator();
	           i.hasNext();
	           ) {
	           Feature feature = (Feature) i.next();
		           
		           GeopistaFeature newfeature = (GeopistaFeature) feature.clone();		           
		           Geometry newGeometry = (Geometry) feature.getGeometry().clone();
		           		           	            
		           ArrayList selectedItems = new ArrayList();
		           for (Iterator j =
		               selectionManagerProxy.getSelectionManager().getSelections().iterator();
		               j.hasNext();
		               ) {
		               IAbstractSelection selection = (IAbstractSelection) j.next();
		               
		               selectedItems.addAll(
		                   selection.items(newGeometry, selection.getSelectedItemIndices(layer, feature)));
		           }
		           setSystemIDNoInitialize(newfeature);
		           newGeometry = editor.edit(newGeometry, selectedItems);
		           		           
		           newfeature.setGeometry(newGeometry);           
		           newGeometry.geometryChanged();	  
		           newfeature.isNew();
		           if(!newfeature.getGeometry().equals(feature.getGeometry())){    	    
		        	   parallelFeatures.add(newfeature);	
		           }
		    
	       }	      
	    }

 
    private void move(Geometry geometry) {
        geometry.apply(new CoordinateFilter() {
            public void filter(Coordinate coordinate) {               
            	Coordinate symmetricfinal = new Coordinate();
            	symmetricfinal = getParallel(coordinate);            	
            	coordinate.setCoordinate(symmetricfinal);
            	
            }
        });
    }
    
    protected Coordinate getParallel(Coordinate point){
       
    		point.x = point.x + n*distance*Px;
    		point.y = point.y + n*distance*Py;
    	
        return point;
    }


    public Cursor getCursor() {
       
    	return Toolkit.getDefaultToolkit().createCustomCursor(
                IconLoader.icon("ParallelCursor.gif").getImage(),
                new java.awt.Point(16, 16),
                getName());
        //return createCursor(IconLoader.icon("SymmetricCursor.gif").getImage(), new Point(4,11));
    	
    }

    public Icon getIcon() {
        return IconLoader.icon("Parallel.gif");
    }

   public void mouseClicked(MouseEvent e) {
        try {        	
        	        	        	
        	if (!checkExactlyNItemsMustBeSelected(1,"ParallelItemsTool")) {        	
                return;
            }      
	        	        
        	super.mouseClicked(e);      
        	
        	if (isFinishingRelease(e)) {                        
        		outlineItemsShape = null;
                Px = 0;
                Py = 0;
                buffer = null;
                distance = 0;
                JTextField factorDistance = (JTextField) getComponentPanel(getAddPanel(toolbox,addPanel),"distance");
                factorDistance.setText("0,0");
                return;
            }
            
            double tolerance = SnapManager.getToleranceInPixels(this.getPanel().getBlackboard()) / this.getPanel().getViewport().getScale();	        	
        	Coordinate[] pointsReference = referenceFeature.getGeometry().getCoordinates();
        	for(int i=0;i<pointsReference.length-1;i++){
        		ArrayList points = new ArrayList();
        		points.add(pointsReference[i]);
        		points.add(pointsReference[i+1]);
        		
        		LineString line = new GeometryFactory().createLineString(toArray(points));
        		Geometry buffergeom = line.buffer(tolerance*this.toleranceFactor);
        		this.setMousePos(getPanel().getViewport().toModelCoordinate(e.getPoint()));
        		Point mousep = new GeometryFactory().createPoint(this.mousePos);
        		    		
            	if (buffergeom.contains(mousep)){
            		
            		buffer = line.buffer((tolerance*this.toleranceFactor)/2);            		
        		    this.outlineItemsShape = getPanel().getJava2DConverter().toShape(buffer);
    		            		        
            		Coordinate point1 = pointsReference[i];
            		Coordinate point2 = pointsReference[i+1];
            		
            		double Sx = (point2.x-point1.x);
            		double Sy = (point2.y -point1.y);            		
            		double dist = (Math.sqrt(Sx*Sx+Sy*Sy));
            		double X = Sx/dist;
            		double Y = Sy/dist;
            		Sx = X;
            		Sy = Y;            		
            		Px = -Sy;
            		Py = Sx;
            		
            	}
        	}
         
           
            
        } catch (Throwable t) {        	
            getPanel().getContext().handleThrowable(t);
        }
    }

        public void activate(ILayerViewPanel layerViewPanel) {
        	
        		super.activate(layerViewPanel);
        		
        		layerViewPanel.getViewport().addListener(new ViewportListener(){
        			public void zoomChanged(Envelope modelEnvelope) {
        				outlineItemsShape = null;			
        			}    			
            	});
        		
        		toolbox = getPanelToolBox();
        		
        		GridBagLayout gridBagLayout = new GridBagLayout();
                JLabel distanceLabel = new JLabel(I18N.get("ParallelItemsTool","distance"));
                final JTextField distanceText = new JTextField(7);
                distanceText.setName("distance");
                distanceText.setText("0,0");
                
                addPanel = new JPanel();
                addPanel.setLayout(gridBagLayout);
                distanceText.addKeyListener(new KeyAdapter()
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
                   public void keyReleased(KeyEvent e)
                   {	        	  
            			   try{	 	    	
            				               				   
            				  distance = readDistance();	        			  
                			  
                			          			  
        		           }catch(Throwable t){
        	            		  getPanel().getContext().handleThrowable(t);
                		   }	    		      	   
                   }	           
                });
                addPanel.add(distanceLabel, new GridBagConstraints(
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
                addPanel.add(distanceText, new GridBagConstraints(
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
                
        		try{
        		
		        	if (!checkExactlyNItemsMustBeSelected(1,"ParallelItemsTool")) {	        		
		                return;
		            }
		        	
		        	referenceFeature = (Feature) layerViewPanel.getSelectionManager().getFeaturesWithSelectedItems().iterator().next();
		        	referenceLayer = (Layer) layerViewPanel.getSelectionManager().getLayersWithSelectedItems().iterator().next();
		        	initialCenter = referenceFeature.getGeometry().getCentroid();
		        	
	        		}
        		catch (Throwable t) {
                    getPanel().getContext().handleThrowable(t);
                }
	        		        	
        }           
        
		public void mousePressed(MouseEvent e) {
            try {   
                
            	if (!checkExactlyNItemsMustBeSelected(1,"ParallelItemsTool")) {        	
                    return;
                }        	
                              
                super.mousePressed(e);                
                fireGestureFinished();    
                
            } catch (Throwable t) {
                getPanel().getContext().handleThrowable(t);
            }
        }
        
        private void setSystemIDNoInitialize(GeopistaFeature feature){
        	
        	GeopistaFeature featuretemp = new GeopistaFeature(feature.getSchema());
        	feature.setSystemId(featuretemp.getSystemId());	         
	                   	
        }
                
        public void cancelGesture() {
                    	
            super.cancelGesture();       
            outlineItemsShape = null;
            Px = 0;
            Py = 0;
            buffer = null;
            distance = 0;
            removePanel(toolbox,addPanel,initialSize); 
            getPanel().getContext().setStatusMessage("");            
            
        }
        
        protected Coordinate[] toArray(ArrayList coordinates) {
            return (Coordinate[]) coordinates.toArray(new Coordinate[] {  });
        }

        protected void setMousePos(Coordinate destination) {
            this.mousePos = snap(destination);
       } 
      
        public void mouseMoved(MouseEvent e){
        	try{
        		    		
        		if(!checkExactlyNItemsMustBeSelected(1,"ParallelItemsTool")){
        			return;
        		}        		        		
        		if(!checkLayersSelectedIsEditable("ParallelItemsTool")){
        			return;
        		}
        		        
        		this.setMousePos(getPanel().getViewport().toModelCoordinate(e.getPoint())); //-- includes snap
        		
        		Coordinate center1 = new Coordinate();
        		Coordinate center2 = new Coordinate();
        		        		
        		center1.x = initialCenter.getX() + distance*Px;
                center1.y = initialCenter.getY() + distance*Py;  
                
                center2.x = initialCenter.getX() - distance*Px;
                center2.y = initialCenter.getY() - distance*Py; 
                
               
        		double num = Math.sqrt(Math.pow((mousePos.x - initialCenter.getX()),2) + Math.pow((mousePos.y - initialCenter.getY()),2));
        		double den = Math.sqrt(Math.pow((center1.x - initialCenter.getX()),2) + Math.pow((center1.y - initialCenter.getY()),2));
        		
        		n = (int) (num/den);
        		
        		center1.x = initialCenter.getX() + n*distance*Px;
                center1.y = initialCenter.getY() + n*distance*Py;  
                
                center2.x = initialCenter.getX() - n*distance*Px;
                center2.y = initialCenter.getY() - n*distance*Py; 
                
                double distance1 = mousePos.distance(center1);
                double distance2 = mousePos.distance(center2);
                
                if(distance1>distance2){
                	n = -n;
                }
        		
                this.outlineItemsShape = null;
                if((n!=0)&&(buffer!=null)&&(distance!=0)){
			        Geometry geoms = (Geometry)this.referenceFeature.getGeometry().clone();
			        this.move(geoms);			                	
	    	    	geoms = geoms.union(this.buffer); 
			        this.outlineItemsShape = getPanel().getJava2DConverter().toShape(geoms);
                }
                else{
                	if(buffer!=null){
                		this.outlineItemsShape = getPanel().getJava2DConverter().toShape(buffer);
                	}
                }
		        
		        redrawShape();		       
                
        	}
            catch (Throwable t) {
            	getPanel().getContext().handleThrowable(t);
            }
        }            
        
        protected Shape getShape(){
        	return this.outlineItemsShape;			
        }
       
        protected boolean isFinishingRelease(MouseEvent e) {
            return e.getClickCount() == 2;
        }
        
        public double readDistance() throws NoninvertibleTransformException{
    		
    		JTextField factorDistance = (JTextField) getComponentPanel(getAddPanel(toolbox,addPanel),"distance");
    				  
    		double distance;
    		if((factorDistance.getText().length()>0)&&(!factorDistance.getText().equals(","))){			
    				distance = Double.parseDouble(factorDistance.getText().replace(',', '.'));    				
    		}
    		else{
    			distance=0;
    		}	
    		return distance;				
    	}
}
