/**
 * ACLayerFamily.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import com.geopista.model.IGeopistaLayer;
import com.geopista.model.LayerFamily;
import com.vividsolutions.jump.workbench.model.ILayerManager;

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
        	IGeopistaLayer layerable = (IGeopistaLayer)itLayers.next();
        	ACLayer acLayer = new ACLayer(layerable.getId_LayerDataBase(),layerable.getName(),layerable.getSystemId(),layerable.getDataSourceQuery().getQuery());
        	acLayer.setRevisionActual(layerable.getRevisionActual());
        	acLayer.setVersionable(layerable.isVersionable());
        	layers.put(layerable.getId_LayerDataBase(), acLayer);
        }
    }

    public ACLayerFamily(ACLayerFamilyBase acLayerFamilySimple) {
		this.id=acLayerFamilySimple.getId();
		this.name=acLayerFamilySimple.getName();
		this.layers=acLayerFamilySimple.getLayers();
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
            //ACLayer acLayer=(ACLayer)this.layers.get(layersIter.next());
            ACLayerBase acLayerBase=(ACLayerBase)this.layers.get(layersIter.next());
            ACLayer acLayer=new ACLayer(acLayerBase);
            
            
            if (htLayers!=null && htLayers.containsKey(String.valueOf(acLayer.getId_layer()))){
                IGeopistaLayer gpLayer=(IGeopistaLayer)htLayers.get(String.valueOf(acLayer.getId_layer()));
                String sXML=(String)htStyleXMLs.get(String.valueOf(acLayer.getId_layer()));
                if (sXML!=null){
                    //acLayer.applyStyle((IGeopistaLayer)gpLayer,(ILayerManager)layerManager,sXML);
                    ACLayerUtil.applyStyle(acLayer,(IGeopistaLayer)gpLayer,(ILayerManager)layerManager,sXML);
                    ACLayerSLD.actualizarReglaPintado((IGeopistaLayer)gpLayer);
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
