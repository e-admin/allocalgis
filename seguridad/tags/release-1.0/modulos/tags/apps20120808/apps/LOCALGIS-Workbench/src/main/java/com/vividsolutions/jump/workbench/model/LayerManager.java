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
package com.vividsolutions.jump.workbench.model;


import com.geopista.model.DynamicLayer;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.Assert;

import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.Reprojector;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.util.Blackboard;

import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.style.AbstractPalettePanel;

import java.awt.Color;
import java.awt.geom.Line2D;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * Registry of Layers in a Task.
 * @see Task
 * @see Layer
 */
public class LayerManager implements ILayerManager{
	
	static Logger logger = Logger.getLogger(LayerManager.class);
	
    private static int layerManagerCount = 0;
    private UndoableEditReceiver undoableEditReceiver = new UndoableEditReceiver();
    private CoordinateSystem coordinateSystem = CoordinateSystem.UNSPECIFIED;

    //As we introduce threaded drawing and other threaded tasks to the
    //workbench, we should be careful not to create situations in which
    //ConcurrentModificationExceptions can occur. [Jon Aquino]
    //Go with List rather than name-to-category map, because category names can
    //now change. [Jon Aquino]
    protected ArrayList categories = new ArrayList();

    //Store weak references to layers rather than the layers themselves -- if a layer
    //is lucky enough to have all its strong references released, let it dispose of
    //itself immediately [Jon Aquino] 
    private ArrayList layerReferencesToDispose = new ArrayList();
    private boolean firingEvents = true;
    private ArrayList layerListeners = new ArrayList();
    private Iterator firstColors;
    private Blackboard blackboard = new Blackboard();

    //Guardamos el orden de las capas para poder elegir el orden de pintado
    private Vector orderLayers = new Vector();

    
    // Modified by Lilian
    public Vector getOrderLayers(){
    	return this.orderLayers;
    }
    
    public LayerManager() {
    	
    	firstColors = firstColors().iterator();
       
        layerManagerCount++;
    }

    public UndoableEditReceiver getUndoableEditReceiver() {
        return undoableEditReceiver;
    }


    /**
     * Permite priorizar la tarea r
     * sobre cualquier otro evento que se este
     * procesando.
     * */
    public void deferFiringEvents(Runnable r) {
        boolean firingEvents = isFiringEvents();
        setFiringEvents(false);

        try {
            r.run();
        } finally {
            setFiringEvents(firingEvents);
        }
    }

    private Collection firstColors() {
        ArrayList firstColors = new ArrayList();
try{
        for (Iterator i = AbstractPalettePanel.basicStyles().iterator();
                i.hasNext();) {
            BasicStyle basicStyle = (BasicStyle) i.next();

            if (!basicStyle.isRenderingFill()) {
                continue;
            }

            firstColors.add(basicStyle.getFillColor());
        }
}catch(Exception ex)
{
	System.out.println("No se ha cargado la paleta de colores.");
	System.out.println(ex.getLocalizedMessage());
}
        return firstColors;
    }

    public Color generateLayerFillColor() {
        //<<TODO>> Ensure that colour is not being used by another layer [Jon Aquino]
        Color color = firstColors.hasNext() ? (Color) firstColors.next()
                                            : new Color((int) Math.floor(
                    Math.random() * 256),
                (int) Math.floor(Math.random() * 256),
                (int) Math.floor(Math.random() * 256));
        color = new Color(color.getRed(), color.getGreen(), color.getBlue());

        return color;
    }

    public ILayer addLayer(String categoryName, ILayer layer) {
        addLayerable(categoryName, layer);

        return layer;
    }
    public void addLayerable(String categoryName, Layerable layerable)
    {
    	addLayerable(categoryName,layerable,-1);
    }
    /**
     * 
     * @param categoryName
     * @param layerable
     * @param positionOnMap -1 significa que se inserta el primero de la lista
     */
    public void addLayerable(String categoryName, Layerable layerable, int positionOnMap) {
        if (layerable instanceof Layer && ((Layer) layerable).getFeatureCollectionWrapper()!= null) {
            if (size() == 0 && getCoordinateSystem() == CoordinateSystem.UNSPECIFIED) {
                setCoordinateSystem(((Layer) layerable).getFeatureCollectionWrapper().getFeatureSchema().getCoordinateSystem());                
            }
            else {
                reproject((Layer) layerable, coordinateSystem);
            }
            layerReferencesToDispose.add(new WeakReference(layerable));
        }

        addCategory(categoryName);

        Category cat = getCategory(categoryName);
        addOrderLayer(positionOnMap,layerable);
        cat.add(-1, layerable);
        //cat.add(positionOnMap, layerable);
        
        //Fire metadata changed so that the visual modified markers are updated. [Jon Aquino]
        fireLayerChanged(layerable, LayerEventType.METADATA_CHANGED);
    }
    
    /**
     * Created by Lilian. Used in GeopistaLoadMapPlugin to load the layer panel. 
     * @param categoryName
     * @param layerable
     * @param positionOnMap -1 significa que se inserta el primero de la lista
     */
    public void addLayerablePanel(String categoryName, Layerable layerable, int positionOnMap) {
        if (layerable instanceof Layer && (!(layerable instanceof DynamicLayer))) {
            if (size() == 0 && getCoordinateSystem() == CoordinateSystem.UNSPECIFIED) {
                setCoordinateSystem(((Layer) layerable).getFeatureCollectionWrapper().getFeatureSchema().getCoordinateSystem());                
            }
            else {
                reproject((Layer) layerable, coordinateSystem);
            }
            layerReferencesToDispose.add(new WeakReference(layerable));
        }

        addCategory(categoryName, 0);

        Category cat = getCategory(categoryName);
        if (positionOnMap > orderLayers.size())
        	positionOnMap = 0;
        orderLayers.insertElementAt(layerable, positionOnMap);
        // Modified by Lilian
        //cat.add(positionOnMap, layerable);
        cat.add(-1, layerable);
        
        //Fire metadata changed so that the visual modified markers are updated. [Jon Aquino]
        //TODO JAVIER
        fireLayerChanged(layerable, LayerEventType.METADATA_CHANGED);
    }
    
/**
 * 
 * @param i -1 significa al inicio de la lista
 * @param layerable
 */
    public void addOrderLayer(int i, Layerable layerable)
    {
    	if (i<0)// orden mal obtenido del mapa (Tambien es 0 cuando no se especifica orden)
    	{
    		orderLayers.insertElementAt(layerable,0); // añade al inicio
    		return;
    	}

    	if(orderLayers.size()<(i+1))
    	{
    		orderLayers.setSize(i+1);
        	orderLayers.set(i,layerable);
    	}else{
    		if (orderLayers.get(i) != null)
    			orderLayers.insertElementAt(layerable, i);
    		else
            	orderLayers.set(i,layerable);
    	}
    }

    public void removeOrderLayer(Layerable layerable)
    {
       orderLayers.remove(layerable);
    }

    public void movePositionLayer(int initPosition, int finalPosition)
    {
      Layerable moveLayer = null;
      
      if(initPosition<finalPosition)
      {
        moveLayer =  (Layerable) orderLayers.get(initPosition);
        orderLayers.setSize(orderLayers.size()+1); 
        orderLayers.add(finalPosition+1,moveLayer);
        orderLayers.remove(initPosition);
      }
      else
      {

        moveLayer =  (Layerable) orderLayers.get(initPosition);
        orderLayers.setSize(orderLayers.size()+1);         
        orderLayers.add(finalPosition,moveLayer);
        orderLayers.remove(initPosition+1);
      }
      fireLayerChanged(moveLayer, LayerEventType.VISIBILITY_CHANGED);

    }
    

    private void reproject(Layer layer, CoordinateSystem coordinateSystem) {
        try {
            Assert.isTrue(indexOf(layer) == -1,
                "If the LayerManager contained this layer, we'd need to be concerned about rolling back on an error [Jon Aquino]");

            if (!Reprojector.instance().wouldChangeValues(layer.getFeatureCollectionWrapper()
                                                                         .getFeatureSchema()
                                                                         .getCoordinateSystem(),
                        coordinateSystem)) {
                return;
            }

            for (Iterator i = layer.getFeatureCollectionWrapper().iterator();
                    i.hasNext();) {
                Feature feature = (Feature) i.next();
                Reprojector.instance().reproject(feature.getGeometry(),
                    layer.getFeatureCollectionWrapper().getFeatureSchema()
                         .getCoordinateSystem(), coordinateSystem);
            }
        } finally {
            //Even if #isReprojectionNecessary returned false, we still need to set
            //the CoordinateSystem to the new value [Jon Aquino]
            layer.getFeatureCollectionWrapper().getFeatureSchema()
                 .setCoordinateSystem(coordinateSystem);
        }
    }

    public void addCategoryInList(String categoryName){}
    
    public void addCategory(String categoryName) {
        addCategory(categoryName, categories.size());
    }

    public void addCategory(String categoryName, int index) {
        if (getCategory(categoryName) != null) {
            return;
        }

        Category category = new Category();
        category.setLayerManager((ILayerManager)this);

        //Can't fire events because this Category hasn't been added to the
        //LayerManager yet. [Jon Aquino]
        boolean firingEvents = isFiringEvents();
        setFiringEvents(false);

        try {
            category.setName(categoryName);
        } finally {
            setFiringEvents(firingEvents);
        }

        // Modified by Lilian to avoid the IndexOutOfBoundsException. 
        // The family Layers are loaded first, but can be placed at the index n with the index n-1 not yet loaded. 
        //if (categories==null || categories.size())
        categories.add(index, category);
        fireCategoryChanged(category, CategoryEventType.ADDED, indexOf(category));
    }

    public Category getCategory(String name) {
        for (Iterator i = categories.iterator(); i.hasNext();) {
            Category category = (Category) i.next();

            if (category!=null && category.getName().equals(name)) {
                return category;
            }
        }

        return null;
    }

    public List getCategories() {
        return Collections.unmodifiableList(categories);
    }

    /**
     * @param name the name of the layer. A number will be appended if a layer
     * with the same name already exists. Set to null to automatically generate a
     * new name.
     */
    public Layer addLayer(String categoryName, String layerName,
        FeatureCollection featureCollection) {
        String actualName = (layerName == null) ? "Layer" : layerName;
        Layer layer = new Layer(actualName, generateLayerFillColor(),
                featureCollection, this);
        addLayerable(categoryName, layer);

        return layer;
    }

    public Layer addOrReplaceLayer(String categoryName, String layerName,
        FeatureCollection featureCollection) {
        Layer oldLayer = getLayer(layerName);

        if (oldLayer != null) {
            remove(oldLayer);
        }

        Layer newLayer = addLayer(categoryName, layerName, featureCollection);

        if (oldLayer != null) {
            newLayer.setStyles(oldLayer.cloneStyles());
        }

        return newLayer;
    }

    /**
     * @return a unique layer name based on the given name
     */
    public String uniqueLayerName(String name) {
        if (!isExistingLayerableName(name)) {
            return name;
        }

        //<<TODO:REMOVE>> debug [Jon Aquino]
        if (name == "Relative Vectors") {
            new Throwable().printStackTrace(System.err);
        }

        int i = 2;
        String newName;

        do {
            newName = name + " (" + i + ")";
            i++;
        } while (isExistingLayerableName(newName));

        return newName;
    }

    private boolean isExistingLayerableName(String name) {
        for (Iterator i = getLayerables(Layerable.class).iterator();
                i.hasNext();) {
            Layerable layerable = (Layerable) i.next();

            if (layerable.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public void remove(Layerable layerable) {
        for (Iterator i = categories.iterator(); i.hasNext();) {
            Category c = (Category) i.next();
            int index = c.indexOf(layerable);

            if (index != -1) {
                c.remove(layerable);
                fireLayerChanged(layerable, LayerEventType.REMOVED, c, index);
            }
        }
    }

    public void removeIfEmpty(Category category) {
        if (!category.isEmpty()) {
            return;
        }

        int categoryIndex = indexOf(category);
        categories.remove(category);
        fireCategoryChanged(category, CategoryEventType.REMOVED, categoryIndex);
    }

    public int indexOf(Category category) {
        return categories.indexOf(category);
    }

    public void fireCategoryChanged(Category category, CategoryEventType type) {
        fireCategoryChanged(category, type, indexOf(category));
    }

    protected void fireCategoryChanged(final Category category,
        final CategoryEventType type, final int categoryIndex) {
        if (!firingEvents) {
            return;
        }

        for (Iterator i = layerListeners.iterator(); i.hasNext();) {
            final LayerListener layerListener = (LayerListener) i.next();
            fireLayerEvent(new Runnable() {
                    public void run() {
                        layerListener.categoryChanged(new CategoryEvent(
                                category, type, categoryIndex));
                    }
                });
        }
    }

    public void fireFeaturesChanged(final Collection features,
        final FeatureEventType type, final ILayer layer) {
        Assert.isTrue(type != FeatureEventType.GEOMETRY_MODIFIED);
        fireFeaturesChanged(features, type, layer, null);
    }

    public void fireGeometryModified(final Collection features,
        final ILayer layer, final Collection oldFeatureClones) {
        Assert.isTrue(oldFeatureClones != null);
        fireFeaturesChanged(features, FeatureEventType.GEOMETRY_MODIFIED,
            layer, oldFeatureClones);
    }

    private void fireFeaturesChanged(final Collection features,
        final FeatureEventType type, final ILayer layer,
        final Collection oldFeatureClones) {
        if (!firingEvents) {
            return;
        }

        //New ArrayList to avoid ConcurrentModificationException [Jon Aquino]
        for (Iterator i = new ArrayList(layerListeners).iterator();
                i.hasNext();) {
            final LayerListener layerListener = (LayerListener) i.next();
            fireLayerEvent(new Runnable() {
                    public void run() {
                        layerListener.featuresChanged(new FeatureEvent(
                                features, type, layer, oldFeatureClones));
                    }
                });
        }
    }

    private void fireLayerEvent(Runnable eventFirer) {
        //In general, LayerListeners are GUI components. Therefore, notify
        //them on the event dispatching thread.[Jon Aquino]
        try {
            GUIUtil.invokeOnEventThread(eventFirer);
        } catch (InterruptedException e) {
            Assert.shouldNeverReachHere();
        } catch (InvocationTargetException e) {
            e.printStackTrace(System.err);
            Assert.shouldNeverReachHere();
        }

        //Note that if the current thread (in which the model was changed) is not
        //the event dispatching thread, the model changes and the listener notifications
        //will be asynchronous -- for example, all the model changes might be done
        //before the listener notifications even begin. This may be a
        //problem for a threaded plug-in that does moderately complex
        //insertions and deletions of nodes in the layer tree (e.g. you may see
        //duplicate tree nodes). If you encounter this problem, try making the
        //model changes (e.g. #addLayer) on the event thread using
        //GUIUtil#invokeOnEventThread. [Jon Aquino]
    }

    /**
     * If layerChangeType is DELETED, layerIndex will of course be the index of
     * the layer in the category prior to removal (not -1).
     */
    private void fireLayerChanged(final Layerable layerable,
        final LayerEventType layerChangeType, final Category category,
        final int layerIndex) {
        if (!firingEvents) {
            return;
        }

        //New ArrayList to avoid ConcurrentModificationException [Jon Aquino]
        //Cuantas mas capas tiene un mapa el numero de layerListeners se incrementa y hace
        //que cada vez sea mas lento invocar los eventos
        //System.out.println("Numero de listeners para la capa:"+layerable.getName()+" :"+layerListeners.size());

        //New ArrayList to avoid ConcurrentModificationException [Jon Aquino]
        for (Iterator i = new ArrayList(layerListeners).iterator();
                i.hasNext();) {
            final LayerListener layerListener = (LayerListener) i.next();
            fireLayerEvent(new Runnable() {
                    public void run() {
                        layerListener.layerChanged(new LayerEvent(layerable,
                                layerChangeType, category, layerIndex));
                    }
                });
        }
    }

    //<<TODO:DESIGN>> Most callers of #fireLayerChanged(Layer, LayerChangeType,
    //LayerCategory, layerIndex) can use this simpler method instead. [Jon Aquino]
    public void fireLayerChanged(Layerable layerable, LayerEventType type) {
        Category cat = getCategory(layerable);

        if (cat == null) {
            Assert.isTrue(!isFiringEvents(),
                "If this event is being fired because you are constructing a Layer, " +
                "cat will be null because you haven't yet added the Layer to the LayerManager. " +
                "While constructing a layer, you should set firingEvents " +
                "to false. (Layerable = " + layerable.getName() + ")");

            return;
        }

        fireLayerChanged(layerable, type, cat, cat.indexOf(layerable));
    }

    public void setFiringEvents(boolean firingEvents) {
        this.firingEvents = firingEvents;
    }

    public boolean isFiringEvents() {
        return firingEvents;
    }

    /**
     * @return an iterator over the layers, from bottom to top. Layers with
     * #drawingLast = true appear last.
     */
    public Iterator reverseIterator(Class layerableClass) {
        ArrayList layerablesCopy = new ArrayList(getLayerables(layerableClass));
        Collections.reverse(layerablesCopy);
        moveLayersDrawnLastToEnd(layerablesCopy);

        return layerablesCopy.iterator();
    }

    private void moveLayersDrawnLastToEnd(List layerables) {
        ArrayList layersDrawnLast = new ArrayList();

        for (Iterator i = layerables.iterator(); i.hasNext();) {
            Layerable layerable = (Layerable) i.next();

            if (!(layerable instanceof Layer)) {
                continue;
            }

            Layer layer = (Layer) layerable;

            if (layer.isDrawingLast()) {
                layersDrawnLast.add(layer);
                i.remove();
            }
        }

        layerables.addAll(layersDrawnLast);
    }

    /**
     * @return Layers, not all Layerables
     */
    public Iterator iterator() {
        //<<TODO:PERFORMANCE>> Create an iterator that doesn't build a Collection of
        //Layers first (unlike #getLayers) [Jon Aquino]
        return getLayers().iterator();
    }

    /**
     * @return null if there is no such layer
     */
    public Layer getLayer(String name) {
        for (Iterator i = iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            if (layer.getName().equals(name)) {
                return layer;
            }
        }

        return null;
    }

    public void addLayerListener(LayerListener layerListener) {
        Assert.isTrue(!layerListeners.contains(layerListener));
        layerListeners.add(layerListener);
    }

    public void removeLayerListener(LayerListener layerListener) {
        layerListeners.remove(layerListener);
    }

    public Layer getLayer(int index) {
        return (Layer) getLayers().get(index);
    }

    public int size() {
        return getLayers().size();
    }

    //<<TODO:MAINTAINABILITY>> Search for uses of #getLayerListModel and see if
    //they can be replaced by #iterator [Jon Aquino]
    public Envelope getEnvelopeOfAllLayers() {
        Envelope envelope = new Envelope();

        for (Iterator i = iterator(); i.hasNext();) {
            Layerable layer = (Layerable) i.next();
            if (layer.getEnvelope()!=null)
            envelope.expandToInclude(layer.getEnvelope());
        }

        return envelope;
    }

    /**
     * @return -1 if the layer does not exist
     */
    public int indexOf(ILayer layer) {
        return getLayers().indexOf(layer);
    }

    public Category getCategory(Layerable layerable) {
        for (Iterator i = categories.iterator(); i.hasNext();) {
            Category category = (Category) i.next();

            if (category!=null && category.contains(layerable)) {
                return category;
            }
        }

        return null;
    }

    public List getLayers() {
        return getLayerables(Layer.class);
    }
    
    public List getWMSLayers() {
        return getLayerables(WMSLayer.class);
    }

    /**
     * To get all Layerables, set layerableClass to Layerable.class.
     */
    public List getLayerables(Class layerableClass) {
        /*Assert.isTrue(Layerable.class.isAssignableFrom(layerableClass));

        ArrayList layers = new ArrayList();

        //Create new ArrayLists to avoid ConcurrentModificationExceptions. [Jon Aquino]
        for (Iterator i = new ArrayList(categories).iterator(); i.hasNext();) {
            Category c = (Category) i.next();

            for (Iterator j = new ArrayList(c.getLayerables()).iterator();
                    j.hasNext();) {
                Layerable l = (Layerable) j.next();

                if (!(layerableClass.isInstance(l))) {
                    continue;
                }

                layers.add(l);
            }
        }
      
        return layers;*/

        Assert.isTrue(Layerable.class.isAssignableFrom(layerableClass));

        ArrayList layers = new ArrayList();

        //Create new ArrayLists to avoid ConcurrentModificationExceptions. [Jon Aquino]
        for (Iterator i = orderLayers.iterator(); i.hasNext();) {
          

        		//TODO Aqui suele dar error de concurrencia
                Layerable l=null;
				l = (Layerable) i.next();
				
                if (!(layerableClass.isInstance(l))) {
                    continue;
                }

                layers.add(l);
         
        }

        return layers;

    }

    public List getVisibleLayers(boolean includeFence) {
        List visibleLayers = getVisibleLayerables(Layer.class,includeFence);
        return visibleLayers;
    }
    public List getVisibleLayerables(Class cl, boolean includeFence) {
        ArrayList visibleLayers = new ArrayList(getLayerables(cl));

        for (Iterator i = visibleLayers.iterator(); i.hasNext();) {
            Layerable layer = (Layerable) i.next();

            if (layer.getName().equals(FenceLayerFinder.LAYER_NAME) &&
                    !includeFence) {
                i.remove();

                continue;
            }

            if (!layer.isVisible()) {
                i.remove();
            }
        }

        return visibleLayers;
    }
    // SIGLE start [obedel]
    // To free the memory allocated for a layer
    public void dispose(Layerable layerable) {
    	for (Iterator i = categories.iterator(); i.hasNext();) {
            Category c = (Category) i.next();
            // deleting the layer from the category
            int index = c.indexOf(layerable);
            if (index != -1) {
                c.remove(layerable);
                for (Iterator j = layerReferencesToDispose.iterator(); j.hasNext();) {
            		WeakReference reference = (WeakReference) j.next();
                   	Layer layer = (Layer) reference.get();
                   	if (layer == layerable) 
                   	{
                		// removing the reference to layer
                   		layer.dispose();
                	   	layerManagerCount--; 	
                	}
                }
                // changing appearance of layer tree
                fireLayerChanged(layerable, LayerEventType.REMOVED, c, index);
            }
        }
    }
    // SIGLE end
    public void dispose() {
        for (Iterator i = layerReferencesToDispose.iterator(); i.hasNext();) {
            WeakReference reference = (WeakReference) i.next();
            Layer layer = (Layer) reference.get();

            if (layer != null) {
                layer.dispose();
            }
        }

        layerManagerCount--;

        //Undo actions may be holding on to expensive resources; therefore, send
        //#die to each to request that the resources be freed. [Jon Aquino]
        undoableEditReceiver.getUndoManager().discardAllEdits();
    }

    public static int layerManagerCount() {
        return layerManagerCount;
    }

    /**
     * Editability is not enforced; all parties are responsible for heeding the
     * editability of a layer.
     */
    public Collection getEditableLayers() {
        ArrayList editableLayers = new ArrayList();

        for (Iterator i = getLayers().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();

            if (layer.isEditable()) {
                editableLayers.add(layer);
            }
        }

        return editableLayers;
    }

    public Blackboard getBlackboard() {
        return blackboard;
    }

    public Collection getLayersWithModifiedFeatureCollections() {
        ArrayList layersWithModifiedFeatureCollections = new ArrayList();

        for (Iterator i = iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();

            if (layer.isFeatureCollectionModified()) {
                layersWithModifiedFeatureCollections.add(layer);
            }
        }

        return layersWithModifiedFeatureCollections;
    }

    public void setCoordinateSystem(CoordinateSystem coordinateSystem) {
        this.coordinateSystem = coordinateSystem;

        //Don't automatically reproject layers here, because I'd like to use an
        //EditTransaction, but that requires a LayerViewPanelContext, which is not
        //available to this LayerManager (but would be available to a plug-in) [Jon Aquino]        
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(Line2D.linesIntersect(708248.882609455,
                2402253.07294874, 708249.523621829, 2402244.3124463,
                708247.896591321, 2402252.48269854, 708261.854734465,
                2402182.39086576));
        System.out.println(new WKTReader().read(
                "LINESTRING(708248.882609455 2402253.07294874, 708249.523621829 2402244.3124463)")
                                          .intersects(new WKTReader().read(
                    "LINESTRING(708247.896591321 2402252.48269854, 708261.854734465 2402182.39086576)")));
    }
	public CoordinateSystem getCoordinateSystem() {
		return coordinateSystem;
	}

}
