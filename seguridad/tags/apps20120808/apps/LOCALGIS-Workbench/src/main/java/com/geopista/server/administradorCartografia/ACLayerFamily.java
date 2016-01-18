package com.geopista.server.administradorCartografia;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.deegree.graphics.sld.Rule;
import org.deegree_impl.graphics.sld.FeatureTypeStyle_Impl;

import com.geopista.model.GeopistaLayer;
import com.geopista.model.IGeopistaLayer;
import com.geopista.model.LayerFamily;
import com.geopista.style.sld.model.SLDFactory;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.style.sld.model.impl.SLDStyleImpl;


import com.geopista.app.AppContext;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.Layerable;

public class ACLayerFamily implements Serializable, IACLayerFamily{
    int id;
    String name;
    String description;
    Hashtable layers;


    
    public ACLayerFamily(){
    }

    public ACLayerFamily(LayerFamily lf){
        this.id=Integer.parseInt(lf.getSystemId());
        this.layers = new Hashtable();
        Iterator itLayers = lf.getLayerables().iterator();
        while (itLayers.hasNext()){
        	GeopistaLayer layerable = (GeopistaLayer)itLayers.next();
        	ACLayer acLayer = new ACLayer(layerable.getId_LayerDataBase(),layerable.getName(),layerable.getSystemId(),layerable.getDataSourceQuery().getQuery());
        	acLayer.setRevisionActual(layerable.getRevisionActual());
        	acLayer.setVersionable(layerable.isVersionable());
        	layers.put(layerable.getId_LayerDataBase(), acLayer);
        }
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayerFamily#getId()
	 */
    @Override
	public int getId() {
        return id;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayerFamily#setId(int)
	 */
    @Override
	public void setId(int id) {
        this.id = id;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayerFamily#getName()
	 */
    @Override
	public String getName() {
        return name;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayerFamily#setName(java.lang.String)
	 */
    @Override
	public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayerFamily#getLayers()
	 */
    @Override
	public Hashtable getLayers() {
        return layers;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayerFamily#setLayers(java.util.Hashtable)
	 */
    @Override
	public void setLayers(Hashtable layers) {
        this.layers = layers;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayerFamily#getDescription()
	 */
    @Override
	public String getDescription() {
        return description;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayerFamily#setDescription(java.lang.String)
	 */
    @Override
	public void setDescription(String description) {
        this.description = description;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayerFamily#convert(com.vividsolutions.jump.workbench.model.ILayerManager, int, java.util.Hashtable, java.util.Hashtable)
	 */
    @Override
	public void convert(ILayerManager layerManager, int iPosition,Hashtable htLayers, Hashtable htStyleXMLs){
        layerManager.addCategory(this.name, iPosition-1); // Las posiciones empiezan en cero?!?!?
        LayerFamily lf=(LayerFamily)layerManager.getCategories().get(iPosition-1);
        lf.setSystemId(String.valueOf(this.getId()));
        lf.setSystemLayerFamily(true);
        Set layersKeys = this.layers.keySet();
        Iterator layersIter = layersKeys.iterator();
        layerManager.setFiringEvents(false);
        while (layersIter.hasNext()){
            ACLayer acLayer=(ACLayer)this.layers.get(layersIter.next());
            if (htLayers!=null && htLayers.containsKey(String.valueOf(acLayer.getId_layer()))){
                IGeopistaLayer gpLayer=(IGeopistaLayer)htLayers.get(String.valueOf(acLayer.getId_layer()));
                String sXML=(String)htStyleXMLs.get(String.valueOf(acLayer.getId_layer()));
                if (sXML!=null){
                    acLayer.applyStyle((GeopistaLayer)gpLayer,(LayerManager)layerManager,sXML);
                    ACLayerSLD.actualizarReglaPintado((GeopistaLayer)gpLayer);
                }
                if (acLayer.getPositionOnMap()!=-1){
                    layerManager.addLayerable(this.name,gpLayer,acLayer.getPositionOnMap());
				}else
                    layerManager.addLayer(this.name,gpLayer);
                
                gpLayer.setLayerManager(layerManager);
            }
        }

    }
    
    
}
