package com.geopista.ui.cursortool.cliptool;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.GeopistaWorkbenchFrame;
import com.geopista.ui.cursortool.DragTool;
import com.geopista.ui.cursortool.cliptool.images.IconLoader;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.geom.EnvelopeUtil;
import com.vividsolutions.jump.util.CollectionMap;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.FenceLayerFinder;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;


import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


public class ClipTool extends DragTool{
	
	private EnableCheckFactory checkFactory;	  
    private ArrayList clipFeatures = new ArrayList();    
    boolean doubleClick = false;
    protected AbstractSelection selection;    
    private Geometry refGeometry = null; 
    
    private Feature refFeature = null;
    private Layer refLayer = null;
    private static AppContext aplicacion = (AppContext) AppContext
    .getApplicationContext();

    public ClipTool(EnableCheckFactory checkFactory) {
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
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.cursortool.cliptool.languages.ClipTooli18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("ClipTool",bundle2);
    }
    
    public String getName(){
    	String name = I18N.get("ClipTool","Clip");
    	return name;
    }

   protected void gestureFinished() throws java.lang.Exception {
        
	   reportNothingToUndoYet();
	   
	   selection = getPanel().getSelectionManager().getFeatureSelection();
       
       refGeometry = (Geometry) getPanel().getSelectionManager().getSelectedItems().iterator().next();
       
       refFeature = (Feature) getPanel().getSelectionManager().getFeatureSelection().getFeaturesWithSelectedItems().iterator().next();
             	
       refLayer = (Layer) getPanel().getSelectionManager().getLayersWithSelectedItems().iterator().next();
	       
                        
        final Collection lastSelected = getPanel().getSelectionManager().getSelectedItems();
         
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
        
        clipFeatures.clear();
        
 	   
 	   final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);
        
        progressDialog.setTitle(I18N.get("ClipTool","RecortandoFeatures"));
        progressDialog.report(I18N.get("ClipTool","RecortandoFeature"));
        final String message = I18N.get("ClipTool","RecortandoFeature");
        progressDialog.addComponentListener(new ComponentAdapter()
        {
 	   public void componentShown(ComponentEvent e)
 	   {
 	     
 	       new Thread(new Runnable()
 	               {
 	           public void run()
 	           {
 	               try
 	               { 	            	
 	            	 
 	            	 int numSelectedItems = getPanel().getSelectionManager().getSelectedItems().size();
 	            	 for (Iterator i = getPanel().getSelectionManager().getFeaturesWithSelectedItems().iterator();
 	                 i.hasNext();
 	                 ) {
 	            		 Feature item = (Feature)i.next();
 	            		if ((item.equals(refFeature))||(!(item.getGeometry().intersects(refGeometry)))){
 	            			 numSelectedItems--;
 	            		 } 	            		 
 	            	 }
 	            	 	            	 
 	            	 int cont=0;
 	            	 for (Iterator i = getPanel().getSelectionManager().getLayersWithSelectedItems().iterator();
 	                 i.hasNext();
 	                 ) {
 	                 	
 	         	    	final Layer layerWithSelectedItems = (Layer) i.next();
 	         	    	
 	         	    	if (layerWithSelectedItems.isEditable()){
 	         	    	
 	         	    		for (Iterator j =
 	         	           		getPanel()
 	         	               .getSelectionManager()
 	         	               .getFeaturesWithSelectedItems(layerWithSelectedItems)
 	         	               .iterator();
 	         	           j.hasNext();
 	         	           ) {
 	         	           final Feature feature = (Feature) j.next();
 	         	           
 	         	           if ((!feature.equals(refFeature))&&(feature.getGeometry().intersects(refGeometry))){
 	         		            	         	        	 
 	         		           final GeopistaFeature newfeature = (GeopistaFeature) feature.clone(); 	         		           
 	         		           final Geometry newGeometry = feature.getGeometry().difference(refGeometry); 	
 	         		           setSystemIDNoInitialize(newfeature);	 	
 	         		         
 	         		           try{
 	         		        	 newfeature.setGeometry(newGeometry);
 	         		          
 	         		           } catch (Throwable t) {  
 	         		        	 
 	         		        	 ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), StringUtil.toFriendlyName(t.getClass()
 	                                    .getName()),GeopistaWorkbenchFrame.toMessage(t) , StringUtil.stackTrace(t));
 	         		           } 	         		            	         		           
 	         		           cont++; 	         		           
 	         		           progressDialog.report(message+cont+"/"+numSelectedItems);
 	         		           
 	         		           execute(new UndoableCommand(getName()) {
 	         				    	public void execute() {                	
 	         				                        
 	         				    		layerWithSelectedItems.getFeatureCollectionWrapper().add(newfeature);
 	         				    		layerWithSelectedItems.getFeatureCollectionWrapper().remove(feature);
 	         				                	
 	         				    	}
 	         				    	public void unexecute() {
 	         				    		layerWithSelectedItems.getFeatureCollectionWrapper().add(feature);
 	         				    		layerWithSelectedItems.getFeatureCollectionWrapper().remove(newfeature); 
 	         				    		if (getPanel().getLayerManager().getUndoableEditReceiver().isAborted()){
 	         				    			getPanel().getLayerManager().getUndoableEditReceiver().setAborted(false);
 	         				    		}
 	         				                	
 	         				    	}
 	         				    });
 	         	           }
 	         	    		}
 	         	    	}
 	                 	else{
 	                 		String warning = I18N.get("ClipTool","SelectedItemsLayersMustBeEditable")+ "(" + layerWithSelectedItems.getName() + ")";
 	         	            getPanel().getContext().warnUser(warning);
 	                 		
 	                 	}
 	                 } 	             
 	            	   
 	               } catch (Exception e) {
 	                   	             
 	               } finally
 	               {
 	                   progressDialog.setVisible(false);
 	                    	                   
 	               }
 	           }
 	               }).start();
 	   }
 	       });
 		GUIUtil.centreOnWindow(progressDialog);
 		progressDialog.setVisible(true);
 		
 		getPanel().getSelectionManager().clear();
 		selection.selectItems(refLayer, refFeature);
 		getPanel().getSelectionManager().setPanelUpdatesEnabled(true);
 		getPanel().getSelectionManager().updatePanel();  
 		 	                
    }
   
    public Cursor getCursor() {
       
    	return Toolkit.getDefaultToolkit().createCustomCursor(
                IconLoader.icon("ClipCursor.gif").getImage(),
                new java.awt.Point(16, 16),
                getName());
        //return createCursor(IconLoader.icon("clipCursor.gif").getImage(), new Point(4,11));
    	
    }

    public Icon getIcon() {
        return IconLoader.icon("Clip.gif");
    }

    public void mouseDragged(MouseEvent e){
    	try {        	

    		if (!checkExactlyNItemsMustBeSelected(1,"ClipTool")) {
    			return;
    		}        	
    		if (!checkLayersSelectedIsEditable("ClipTool")) {
    			return;
    		}    	   

    		super.mouseDragged(e);


    	} catch (Throwable t) {        	
    		getPanel().getContext().handleThrowable(t);
    	}
    }
   public void mouseClicked(MouseEvent e) {
        try {        	
        	
        	if (!checkExactlyNItemsMustBeSelected(1,"ClipTool")) {
                return;
            }        	
            if (!checkLayersSelectedIsEditable("ClipTool")) {
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
        		
	        	if (!checkExactlyNItemsMustBeSelected(1,"ClipTool")) {
	                return;
	            }
	            if (!checkLayersSelectedIsEditable("ClipTool")) {
	                return;
	            }    	   
	            
        
        }  
               
        public void mousePressed(MouseEvent e) {
            try {    
                
            	if (!checkExactlyNItemsMustBeSelected(1,"ClipTool")) {
	                return;
	            }	        	
	            if (!checkLayersSelectedIsEditable("ClipTool")) {
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
