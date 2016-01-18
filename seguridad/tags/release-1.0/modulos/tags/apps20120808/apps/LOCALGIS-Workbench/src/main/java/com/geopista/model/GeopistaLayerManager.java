package com.geopista.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.CategoryEventType;
import com.vividsolutions.jump.workbench.model.ILayer;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEventType;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.Layerable;

public class GeopistaLayerManager extends LayerManager 
{

    private boolean isDirty = false;
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    /* (non-Javadoc)
	 * @see com.geopista.model.a#addLayer(java.lang.String, java.lang.String, com.vividsolutions.jump.feature.FeatureCollection)
	 */
    @Override
	public Layer addLayer(String categoryName, String layerName,
            FeatureCollection featureCollection)
    {
        setDirty(true);
        String actualName = (layerName == null) ? aplicacion.getI18nString("Layer") : layerName;
        GeopistaLayer layer = new GeopistaLayer(actualName, generateLayerFillColor(),
                featureCollection, this);
        layer.setLocal(true);
        
        addLayerable(categoryName, layer);

        return layer;
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.a#addOrReplaceLayer(java.lang.String, java.lang.String, com.vividsolutions.jump.feature.FeatureCollection)
	 */
    @Override
	public Layer addOrReplaceLayer(String categoryName, String layerName,
            FeatureCollection featureCollection)
    {
        setDirty(true);
        GeopistaLayer oldLayer = (GeopistaLayer) getLayer(layerName);

        if (oldLayer != null)
        {
            remove(oldLayer);
        }

        Layer newLayer = addLayer(categoryName, layerName, featureCollection);

        if (oldLayer != null)
        {
            newLayer.setStyles(oldLayer.cloneStyles());
        }

        return newLayer;
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.a#getLayers()
	 */
    @Override
	public List getLayers()
    {
        return getLayerables(GeopistaLayer.class);
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.a#getLayer(java.lang.String)
	 */
    @Override
	public Layer getLayer(String systemId)
    {
        for (Iterator i = iterator(); i.hasNext();)
        {
            Layer layer = (Layer) i.next();

//            if (((GeopistaLayer) layer).getSystemId().equals(systemId))
            if (((GeopistaLayer) layer).getSystemId().equals(systemId))
            {
                return layer;
            }
        }

        return null;
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.a#addCategoryInList(java.lang.String)
	 */
    @Override
	public void addCategoryInList(String categoryName){
    	setDirty(true);
        if (getCategory(categoryName) != null)
        {
            return;
        }

        LayerFamily category = new LayerFamily();
        category.setLayerManager(this);

        
        boolean firingEvents = isFiringEvents();
        setFiringEvents(false);

        try
        {
            category.setName(categoryName);
        } finally
        {
            setFiringEvents(firingEvents);
        }

// 		The family Layers are loaded first, but can be placed at the index n with the index n-1 not yet loaded. 
		if (categories==null){
			categories = new ArrayList();
		}

		categories.add(0, category);	
		
        fireCategoryChanged(category, CategoryEventType.ADDED, indexOf(category));
    }
    
    /* (non-Javadoc)
	 * @see com.geopista.model.a#addCategory(java.lang.String, int)
	 */
    @Override
	public void addCategory(String categoryName, int index)
    {
        setDirty(true);
        if (getCategory(categoryName) != null)
        {
            return;
        }

        LayerFamily category = new LayerFamily();
        category.setLayerManager(this);

        // Can't fire events because this Category hasn't been added to the
        // LayerManager yet. [Jon Aquino]
        boolean firingEvents = isFiringEvents();
        setFiringEvents(false);

        try
        {
            category.setName(categoryName);
        } finally
        {
            setFiringEvents(firingEvents);
        }
 
// 		The family Layers are loaded first, but can be placed at the index n with the index n-1 not yet loaded. 
		if (categories==null){
			categories = new ArrayList();
		}else{
			while (categories.size()<=index){
				categories.add(null);
			}
		}
		categories.set(index, category);	
		
        fireCategoryChanged(category, CategoryEventType.ADDED, indexOf(category));
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.a#getSystemCategories()
	 */
	public List getSystemCategories()
    {
        ArrayList layerFamilies = new ArrayList(categories);
        List systemFamilies = new ArrayList();
        Iterator layerFamiliesIter = layerFamilies.iterator();
        while (layerFamiliesIter.hasNext())
        {
            LayerFamily layerFamily = (LayerFamily) layerFamiliesIter.next();
            if (layerFamily.isSystemLayerFamily())
            {
                systemFamilies.add(layerFamily);
            }

        }
        return systemFamilies;
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.a#isDirty()
	 */
	public boolean isDirty()
    {
        return isDirty;
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.a#setDirty(boolean)
	 */
	public void setDirty(boolean isDirty)
    {
        this.isDirty = isDirty;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vividsolutions.jump.workbench.model.LayerManager#addCategory(java.lang.String)
     */
    /* (non-Javadoc)
	 * @see com.geopista.model.a#addCategory(java.lang.String)
	 */
    @Override
	public void addCategory(String categoryName)
    {
        // TODO Auto-generated method stub
        setDirty(true);
        super.addCategory(categoryName);
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vividsolutions.jump.workbench.model.LayerManager#addLayer(java.lang.String,
     *      com.vividsolutions.jump.workbench.model.Layer)
     */
    /* (non-Javadoc)
	 * @see com.geopista.model.a#addLayer(java.lang.String, com.vividsolutions.jump.workbench.model.Layer)
	 */
	public ILayer addLayer(String categoryName, Layer layer)
    {
        // TODO Auto-generated method stub
        setDirty(true);
        return super.addLayer(categoryName, layer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vividsolutions.jump.workbench.model.LayerManager#addLayerable(java.lang.String,
     *      com.vividsolutions.jump.workbench.model.Layerable, int)
     */
    /* (non-Javadoc)
	 * @see com.geopista.model.a#addLayerable(java.lang.String, com.vividsolutions.jump.workbench.model.Layerable, int)
	 */
    @Override
	public void addLayerable(String categoryName, Layerable layerable, int positionOnMap)
    {
        // TODO Auto-generated method stub
        setDirty(true);
        super.addLayerable(categoryName, layerable, positionOnMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vividsolutions.jump.workbench.model.LayerManager#addLayerable(java.lang.String,
     *      com.vividsolutions.jump.workbench.model.Layerable)
     */
    /* (non-Javadoc)
	 * @see com.geopista.model.a#addLayerable(java.lang.String, com.vividsolutions.jump.workbench.model.Layerable)
	 */
    @Override
	public void addLayerable(String categoryName, Layerable layerable)
    {
        // TODO Auto-generated method stub
        setDirty(true);
        super.addLayerable(categoryName, layerable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vividsolutions.jump.workbench.model.LayerManager#addLayerListener(com.vividsolutions.jump.workbench.model.LayerListener)
     */
    /* (non-Javadoc)
	 * @see com.geopista.model.a#addLayerListener(com.vividsolutions.jump.workbench.model.LayerListener)
	 */
    @Override
	public void addLayerListener(LayerListener layerListener)
    {
        // TODO Auto-generated method stub
        setDirty(true);
        super.addLayerListener(layerListener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vividsolutions.jump.workbench.model.LayerManager#addOrderLayer(int,
     *      com.vividsolutions.jump.workbench.model.Layerable)
     */
    /* (non-Javadoc)
	 * @see com.geopista.model.a#addOrderLayer(int, com.vividsolutions.jump.workbench.model.Layerable)
	 */
    @Override
	public void addOrderLayer(int i, Layerable layerable)
    {
        // TODO Auto-generated method stub
        setDirty(true);
        super.addOrderLayer(i, layerable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vividsolutions.jump.workbench.model.LayerManager#movePositionLayer(int,
     *      int)
     */
    /* (non-Javadoc)
	 * @see com.geopista.model.a#movePositionLayer(int, int)
	 */
    @Override
	public void movePositionLayer(int initPosition, int finalPosition)
    {
        // TODO Auto-generated method stub
    	//setFiringEvents(false);
    	if (initPosition!=finalPosition){
	        setDirty(true);
	        super.movePositionLayer(initPosition, finalPosition);
    	}
    	//setFiringEvents(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vividsolutions.jump.workbench.model.LayerManager#remove(com.vividsolutions.jump.workbench.model.Layerable)
     */
    /* (non-Javadoc)
	 * @see com.geopista.model.a#remove(com.vividsolutions.jump.workbench.model.Layerable)
	 */
    @Override
	public void remove(Layerable layerable)
    {
        // TODO Auto-generated method stub
        setDirty(true);
        super.remove(layerable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vividsolutions.jump.workbench.model.LayerManager#removeIfEmpty(com.vividsolutions.jump.workbench.model.Category)
     */
    /* (non-Javadoc)
	 * @see com.geopista.model.a#removeIfEmpty(com.vividsolutions.jump.workbench.model.Category)
	 */
    @Override
	public void removeIfEmpty(Category category)
    {
        // TODO Auto-generated method stub
        setDirty(true);
        super.removeIfEmpty(category);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vividsolutions.jump.workbench.model.LayerManager#removeLayerListener(com.vividsolutions.jump.workbench.model.LayerListener)
     */
    /* (non-Javadoc)
	 * @see com.geopista.model.a#removeLayerListener(com.vividsolutions.jump.workbench.model.LayerListener)
	 */
    @Override
	public void removeLayerListener(LayerListener layerListener)
    {
        // TODO Auto-generated method stub
        setDirty(true);
        super.removeLayerListener(layerListener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vividsolutions.jump.workbench.model.LayerManager#removeOrderLayer(com.vividsolutions.jump.workbench.model.Layerable)
     */
    /* (non-Javadoc)
	 * @see com.geopista.model.a#removeOrderLayer(com.vividsolutions.jump.workbench.model.Layerable)
	 */
    @Override
	public void removeOrderLayer(Layerable layerable)
    {
        // TODO Auto-generated method stub
        setDirty(true);
        super.removeOrderLayer(layerable);
    }
    
    
    /* (non-Javadoc)
	 * @see com.geopista.model.a#sortLayers(java.util.ArrayList)
	 */
    
	public void sortLayers(ArrayList lstSortedLayers)
    {
    	//ArrayList preSort = new ArrayList(getOrderLayers());
    	for (int i=0; i<lstSortedLayers.size(); i++)
    	{
    		int index = getOrderLayers().indexOf(lstSortedLayers.get(i));
    		
    		if (index!=i)
    		{
    			movePositionLayer(index, i);
    		}  
    	}
    } 
    
}