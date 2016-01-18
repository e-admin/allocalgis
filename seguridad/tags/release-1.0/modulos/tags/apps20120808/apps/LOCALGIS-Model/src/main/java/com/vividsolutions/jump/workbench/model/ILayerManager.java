package com.vividsolutions.jump.workbench.model;


import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.util.Blackboard;
import java.awt.Color;
import java.util.*;


/**
 * Registry of Layers in a Task.
 * @see Task
 * @see Layer
 */
public interface ILayerManager {

	    public Vector getOrderLayers();
	    	
	   
	    public UndoableEditReceiver getUndoableEditReceiver();

	    	public void deferFiringEvents(Runnable r);

	  

	    public Color generateLayerFillColor();
	    public ILayer addLayer(String categoryName, ILayer layer);
	     
	    public void addLayerable(String categoryName, Layerable layerable, int positionOnMap);
	    public void addLayerable(String categoryName, Layerable layerable);
	    
	    
	    public void addLayerablePanel(String categoryName, Layerable layerable, int positionOnMap);		
	    		
	    public void addOrderLayer(int i, Layerable layerable);
	    
	    public void removeOrderLayer(Layerable layerable);
	    

	    public void movePositionLayer(int initPosition, int finalPosition);
	    
	    
	    public void addCategoryInList(String categoryName);
	    
	    public void addCategory(String categoryName);

	    public void addCategory(String categoryName, int index);

	    public Category getCategory(String name);

	    public List getCategories();
	    
	    public Layer addLayer(String categoryName, String layerName,
	        FeatureCollection featureCollection);

	    public Layer addOrReplaceLayer(String categoryName, String layerName,
	        FeatureCollection featureCollection);
	    
	    public String uniqueLayerName(String name);

	    public void remove(Layerable layerable);
	    
	    public void removeIfEmpty(Category category);

	    public int indexOf(Category category);
	    
	    public void fireCategoryChanged(Category category, CategoryEventType type);

	    public void fireFeaturesChanged(final Collection features,
	            final FeatureEventType type, final ILayer layer);

	        public void fireGeometryModified(final Collection features,
	            final ILayer layer, final Collection oldFeatureClones);
	    
	    public void fireLayerChanged(Layerable layerable, LayerEventType type);

	    public void setFiringEvents(boolean firingEvents);

	    public boolean isFiringEvents();

	   
	    public Iterator reverseIterator(Class layerableClass);
	    
	   
	    
	    public Iterator iterator();
	    
	    public Layer getLayer(String name);
	    
	    public void addLayerListener(LayerListener layerListener);
	    
	    public void removeLayerListener(LayerListener layerListener);

	    public Layer getLayer(int index);

	    public int size();

	   
	    public Envelope getEnvelopeOfAllLayers();
	    
	    public int indexOf(ILayer layer);
	    
	    public Category getCategory(Layerable layerable);

	    public List getLayers();
	    
	    public List getWMSLayers();
	    
	    public List getLayerables(Class layerableClass);

	    public List getVisibleLayers(boolean includeFence);
	    
	    public List getVisibleLayerables(Class cl, boolean includeFence);
	    
	    public void dispose(Layerable layerable);
	    
	    // SIGLE end
	    public void dispose();

	   
	    public Collection getEditableLayers();

	    public Blackboard getBlackboard();
	    
	    public Collection getLayersWithModifiedFeatureCollections();
	    
	    public void setCoordinateSystem(CoordinateSystem coordinateSystem);

		public CoordinateSystem getCoordinateSystem();
	}

