package com.geopista.ui.cursortool.symmetrictool;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.cursortool.DragTool;
import com.geopista.ui.cursortool.symmetrictool.images.IconLoader;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.geom.EnvelopeUtil;
import com.vividsolutions.jump.util.CollectionMap;
import com.vividsolutions.jump.workbench.model.FenceLayerFinder;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.SelectionManagerProxy;
import com.vividsolutions.jump.workbench.ui.EditTransaction.SelectionEditor;




public class SymmetricTool extends DragTool{
	
	private EnableCheckFactory checkFactory;
	private Feature symmetricAxisFeature;   
	private Layer symmetricAxisLayer;
    private ArrayList symmetricFeatures = new ArrayList();
    private Coordinate[] points = new Coordinate[2];
    private int numpoints;
    boolean doubleClick = false;
    protected AbstractSelection selection;
    
    private static AppContext aplicacion = (AppContext) AppContext
    .getApplicationContext();

    public SymmetricTool(EnableCheckFactory checkFactory) {
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
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.cursortool.symmetrictool.languages.SymmetricTooli18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("SymmetricTool",bundle2);
    }
    
    public String getName(){
    	String name = I18N.get("SymmetricTool","Symmetric");
    	return name;
    }

   protected void gestureFinished() throws java.lang.Exception {
        
	   reportNothingToUndoYet();
                        
        Collection lastSelected = getPanel().getSelectionManager().getSelectedItems();
         
        getPanel().getSelectionManager().clear();
              
        Map layerToFeaturesInFenceMap =
            getPanel().visibleLayerToFeaturesInFenceMap(
                EnvelopeUtil.toGeometry(getBoxInModelCoordinates()));

        Collection layers = layerToFeaturesInFenceMap.keySet();
        if (selectedLayersOnly()) {
            layers.retainAll(Arrays.asList(getTaskFrame().getLayerNamePanel().getSelectedLayers()));
        }
        if(layers.isEmpty())
        {
          if(!lastSelected.isEmpty())
          {
            if(getIWorkbench().getGuiComponent() instanceof GeopistaEditor)
            {
              ((GeopistaEditor) getIWorkbench().getGuiComponent()).fireGeopistaSelectionChanged(selection);
            }
          }
        }
        for (Iterator i = layers.iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();

            if (layer.getName().equals(FenceLayerFinder.LAYER_NAME)) {
                continue;
            }

            if (!((GeopistaLayer)layer).isActiva()) {
                continue;
            }

            //Disable panel updates -- we'll manually repaint the selection and
            //fire the selection-changed event. [Jon Aquino]
            boolean originalPanelUpdatesEnabled =
                getPanel().getSelectionManager().arePanelUpdatesEnabled();
            getPanel().getSelectionManager().setPanelUpdatesEnabled(false);
            try {
                CollectionMap featureToItemsToSelectMap =
                    featureToItemsInFenceMap(
                        (Collection) layerToFeaturesInFenceMap.get(layer),
                        layer,
                        false);
                CollectionMap featureToItemsToUnselectMap =
                    featureToItemsInFenceMap(
                        (Collection) layerToFeaturesInFenceMap.get(layer),
                        layer,
                        true);
                 selection.selectItems(layer, featureToItemsToSelectMap);
                 if(isDoubleClick())
                 {
                    if(getIWorkbench().getGuiComponent() instanceof GeopistaEditor)
                    {
                      ((GeopistaEditor) getIWorkbench().getGuiComponent()).fireGeopistaFeatureActioned(selection);
                    }
                 }
                        
                if (wasShiftPressed()) {
                    selection.unselectItems(layer, featureToItemsToUnselectMap);
                    if(!featureToItemsToUnselectMap.isEmpty())
                    {
                        if(!isDoubleClick())
                        {
                          if(getIWorkbench().getGuiComponent() instanceof GeopistaEditor)
                          {
                            ((GeopistaEditor) getIWorkbench().getGuiComponent()).fireGeopistaSelectionChanged(selection);
                          }
                        }
                    }
                }
                    else
                    {
                        if(!featureToItemsToSelectMap.isEmpty())
                        {
                          
                          if(!isDoubleClick())
                          {
                            Collection local = selection.getSelectedItems();
                            if(!(local.containsAll(lastSelected)&&(lastSelected.containsAll(local))))
                            {
                              if(getIWorkbench().getGuiComponent() instanceof GeopistaEditor)
                              {
                                ((GeopistaEditor) getIWorkbench().getGuiComponent()).fireGeopistaSelectionChanged(selection);
                              }
                            }
                          }
                        }
                 
                }
                featureToItemsInFenceMap(
                    (Collection) layerToFeaturesInFenceMap.get(layer),
                    layer,
                    true);
            } finally {
                setDoubleClick(false);
                getPanel().getSelectionManager().setPanelUpdatesEnabled(
                    originalPanelUpdatesEnabled);
            }
        }

        getPanel().getSelectionManager().updatePanel();   
        
        symmetricFeatures.clear();
        for (Iterator i = getPanel().getSelectionManager().getLayersWithSelectedItems().iterator();
        i.hasNext();
        ) {        	
	    	final Layer layerWithSelectedItems = (Layer) i.next();
	    	
	    	if (layerWithSelectedItems.isEditable()){
	    	
	            setSymmetricFeatures(layerWithSelectedItems);
	    	}
        	else{        		
        		String warning = I18N.get("SymmetricTool","SelectedItemsLayersMustBeEditable") + " (" + layerWithSelectedItems.getName() + ")";
	            getPanel().getContext().warnUser(warning);        		
        	}
        }
              
	    boolean doExecute = false;
	    int cursedfeatures=0;
	   	            
	    for (Iterator j = symmetricFeatures.iterator(); j.hasNext();) {
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
 		        	if((symmetricFeatures.size()-cursedfeatures)>0){
 		        		//Mostrar diálogo de cancelar actual o cancelar todas
 		        		if (JOptionPane.showConfirmDialog(
	 		                            (Component) getIWorkbench().getGuiComponent(),
	 		                           I18N.get("SymmetricTool","GeopistaSymmetricTool.CancellAll"),
	 		                           I18N.get("SymmetricTool","GeopistaSymmetricTool.Cancell"),
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
	             
	    getPanel().getSelectionManager().clear();
 		selection.selectItems(symmetricAxisLayer,symmetricAxisFeature);
 		getPanel().getSelectionManager().setPanelUpdatesEnabled(true);
 		getPanel().getSelectionManager().updatePanel(); 
	 
    }
   
   private void setSymmetricFeatures(Layer layer) throws NoninvertibleTransformException, ClassNotFoundException, IllegalAccessException, InstantiationException {
       createSymmetricFeatures(new EditTransaction.SelectionEditor() {
          public Geometry edit(Geometry geometryWithSelectedItems, Collection selectedItems) {            	
              for (Iterator j = selectedItems.iterator(); j.hasNext();) {
                  Geometry item = (Geometry) j.next();                                     
                  move(item);         
              }
              return geometryWithSelectedItems;   
          }
      }, getPanel(), layer);
     
  }
   
   public void createSymmetricFeatures(
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
	           
	           if (!feature.equals(symmetricAxisFeature)){
		           
		           GeopistaFeature newfeature = (GeopistaFeature) feature.clone();
		           
		           Geometry newGeometry = (Geometry) feature.getGeometry().clone();
		                     
		           ArrayList selectedItems = new ArrayList();
		           for (Iterator j =
		               selectionManagerProxy.getSelectionManager().getSelections().iterator();
		               j.hasNext();
		               ) {
		               AbstractSelection selection = (AbstractSelection) j.next();
		               //Use #getSelectedItemIndices rather than #getSelectedItems, because
		               //we want the selected items from newGeometry, not the original
		               //Geometry (so that editor can freely modify them). [Jon Aquino]
		               selectedItems.addAll(
		                   selection.items(newGeometry, selection.getSelectedItemIndices(layer, feature)));
		           }
		           setSystemIDNoInitialize(newfeature);
		           newGeometry = editor.edit(newGeometry, selectedItems);
		           		           
		           newfeature.setGeometry(newGeometry);           
		           newGeometry.geometryChanged();	  
		           newfeature.isNew();
		           		           	           	    
		           symmetricFeatures.add(newfeature);	
		           
	           }
	       }	      
	    }

 
    private void move(Geometry geometry) {
        geometry.apply(new CoordinateFilter() {
            public void filter(Coordinate coordinate) {               
            	Coordinate symmetricfinal = new Coordinate();
            	symmetricfinal = getSymmetric(coordinate);            	
            	coordinate.setCoordinate(symmetricfinal);
            	
            }
        });
    }
    
    protected Coordinate getSymmetric(Coordinate point2){
        
        Coordinate point4 = new Coordinate();
        Coordinate point3 = new Coordinate();
          
        if(numpoints == 2){
	        double lambda = ( point2.x*(points[1].x - points[0].x) - points[0].x*(points[1].x - points[0].x) + point2.y*(points[1].y - points[0].y) - points[0].y*(points[1].y - points[0].y))/(((points[1].x - points[0].x)*(points[1].x - points[0].x))+((points[1].y - points[0].y)*(points[1].y - points[0].y)));
	        point3.x = points[0].x + lambda*(points[1].x-points[0].x);
	        point3.y = points[0].y + lambda*(points[1].y-points[0].y);
	        point4.x = 2*point3.x - point2.x;
	        point4.y = 2*point3.y - point2.y;       
        }
        else{
        	point3.x = points[0].x;
        	point3.y = points[0].y;
        	point4.x = 2*point3.x - point2.x;
	        point4.y = 2*point3.y - point2.y;
        }
        
        return point4;
    }


    public Cursor getCursor() {
       
    	return Toolkit.getDefaultToolkit().createCustomCursor(
                IconLoader.icon("SymmetricCursor.gif").getImage(),
                new java.awt.Point(10, 14),
                getName());
        //return createCursor(IconLoader.icon("SymmetricCursor.gif").getImage(), new Point(4,11));
    	
    }

    public Icon getIcon() {
        return IconLoader.icon("simetria.gif");
    }

   public void mouseClicked(MouseEvent e) {
        try {        	
        	        	
        	if (!checkExactlyNItemsMustBeSelected(1,"SymmetricTool")) {        	
                return;
            }        	
              
	        if ((numpoints!= 2) && (numpoints!= 1)) {
	            String warning = I18N.get("SymmetricTool","SelectLineStringOrPoint");
	            getPanel().getContext().warnUser(warning);
	            return;
	        }
	        
        	super.mouseClicked(e);
            setViewSource(e.getPoint());
            setViewDestination(e.getPoint());
            if (e.getClickCount() == 2){
                setDoubleClick(true);
           }
           fireGestureFinished();
            
        } catch (Throwable t) {        	
            getPanel().getContext().handleThrowable(t);
        }
    }

    protected boolean selectedLayersOnly() {
        return wasControlPressed();
    }
	
    private CollectionMap featureToItemsInFenceMap(
            Collection features,
            Layer layer,
            boolean selected) throws NoninvertibleTransformException 
             {
            CollectionMap featureToSelectedItemsMap =
                selection.getFeatureToSelectedItemCollectionMap(layer);
            CollectionMap featureToItemsInFenceMap = new CollectionMap();
            for (Iterator i = features.iterator(); i.hasNext();) {
                Feature feature = (Feature) i.next();
                Collection selectedItems = featureToSelectedItemsMap.getItems(feature);
                Collection itemsToReturn = itemsInFence(feature);
                if (selected) {
                    itemsToReturn.retainAll(selectedItems);
                } else {
                    itemsToReturn.removeAll(selectedItems);
                }
                featureToItemsInFenceMap.put(feature, itemsToReturn);
            }

            return featureToItemsInFenceMap;
        }

        private Collection itemsInFence(Feature feature) throws NoninvertibleTransformException  {
            ArrayList itemsInFence = new ArrayList();
            
            Geometry fence = EnvelopeUtil.toGeometry(getBoxInModelCoordinates());
            
            for (Iterator i = selection.items(feature.getGeometry()).iterator(); i.hasNext();) {
                Geometry selectedItem = (Geometry) i.next();

                if (LayerViewPanel.intersects(selectedItem, fence)) {
                    itemsInFence.add(selectedItem);
                }
            }

            return itemsInFence;
        }
        
        public boolean isDoubleClick()
        {
          return doubleClick;
        }

        public void setDoubleClick(boolean doubleClick)
        {
          this.doubleClick=doubleClick;
        }

        public void activate(ILayerViewPanel layerViewPanel) {
        	
        		super.activate(layerViewPanel);
        		
	        	if (!checkExactlyNItemsMustBeSelected(1,"SymmetricTool")) {
	                return;
	            }
	        		    	    
	            numpoints = countPointsSelectedLineString(layerViewPanel);
	            
		        if ((numpoints!= 2) && (numpoints!= 1)) {
		            String warning = I18N.get("SymmetricTool","SelectLineStringOrPoint");
		            layerViewPanel.getContext().warnUser(warning);
		            return;
		        }
		        	           
	            selection = ((LayerViewPanel)layerViewPanel).getSelectionManager().getFeatureSelection();
	            
	            getverticesLineString(layerViewPanel);
	            setSymmetricAxis(layerViewPanel);  
	            symmetricAxisLayer = (Layer) getPanel().getSelectionManager().getLayersWithSelectedItems().iterator().next();
        
        }   
        
        private int countPointsSelectedLineString(ILayerViewPanel layerViewPanel){
        	
        	int numPoints = 0;
        	        
                for (Iterator i = layerViewPanel.getSelectionManager().getSelectedItems().iterator();
                    i.hasNext();
                    ) {
                    Geometry selectedItem = (Geometry) i.next();
                    Coordinate[] coordinates = selectedItem.getCoordinates();
                    numPoints = coordinates.length;

                }
				return numPoints;
        }
        
        private void setSymmetricAxis(ILayerViewPanel layerViewPanel){
        	Feature feature = null;
        	for (Iterator i =
        			((LayerViewPanel)layerViewPanel)
 	               .getSelectionManager()
 	               .getFeatureSelection().getFeaturesWithSelectedItems().iterator();
 	           i.hasNext();
 	           ) {
 	           feature = (Feature) i.next();
 	           
        	}
        	symmetricAxisFeature = feature;
        	
        }
        
        private Coordinate[] getverticesLineString(ILayerViewPanel layerViewPanel) {
            //Lazily initialized because not used if there are no snapping policies. [Jon Aquino]                   
           
                for (Iterator i = layerViewPanel.getSelectionManager().getSelectedItems().iterator();
                    i.hasNext();
                    ) {
                    Geometry selectedItem = (Geometry) i.next();
                    Coordinate[] coordinates = selectedItem.getCoordinates();

                    for (int j = 0; j < coordinates.length; j++) { 
                        
                     		points[j]= coordinates[j];
                    }
                }
                
             return points;
        }
        public void mousePressed(MouseEvent e) {
            try {            	
            	
            	if (!checkExactlyNItemsMustBeSelected(1,"SymmetricTool")) {        	
                    return;
                }        	                  
    	        if ((numpoints!= 2) && (numpoints!= 1)) {
    	            String warning = I18N.get("SymmetricTool","SelectLineStringOrPoint");
    	            getPanel().getContext().warnUser(warning);
    	            return;
    	        }
               
                super.mousePressed(e);
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
            getPanel().getContext().setStatusMessage("");
        }

}
