/**
 * ACLayerUtil.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.deegree_impl.graphics.sld.UserStyle_Impl;

import com.geopista.model.DynamicLayer;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.IGeopistaLayer;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.SquareVertexStyle;

public class ACLayerUtil {
	
	private static final Log	logger	= LogFactory.getLog(ACLayerUtil.class);
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#convert(com.vividsolutions.jump.workbench.model.LayerManager)
	 */
	public static GeopistaLayer convert(IACLayer acLayer,ILayerManager layerManager){
        GeopistaLayer lRet=new GeopistaLayer();
        lRet.setId_LayerDataBase(acLayer.getId_layer());
        lRet.setSystemId(acLayer.getSystemName());
        lRet.setName(acLayer.getName());
        lRet.setActiva(acLayer.isActive());
        lRet.setVisible(acLayer.isVisible());
        lRet.setEditable(acLayer.isEditable());
        lRet.setFieldExtendedForm(acLayer.getExtendedForm());
        lRet.setRevisionActual(acLayer.getRevisionActual());
        lRet.setVersionable(acLayer.isVersionable());
        lRet.setUltimaRevision(acLayer.getUltimaRevision());
        if (layerManager!=null){
            lRet.setLayerManager(layerManager);
            if (acLayer.getStyleXML()!=null)
                applyStyle((ACLayer)acLayer,lRet,layerManager,acLayer.getStyleXML());
        }
        return lRet;
    }
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#applyStyle(com.geopista.model.GeopistaLayer, com.vividsolutions.jump.workbench.model.LayerManager, java.lang.String)
	 */
	
	public static void applyStyle(ACLayer acLayer,IGeopistaLayer gpLayer,ILayerManager layerManager,String sXML){
        boolean bFiringEvents=layerManager.isFiringEvents();
        try{
            layerManager.setFiringEvents(false);
            gpLayer.setLayerManager(layerManager);
            List lStyles=new ArrayList();
            SLDStyleImpl sld=new SLDStyleImpl(sXML,gpLayer.getSystemId());
            sld.setSystemId(acLayer.getStyleId());
            String sStyleName=acLayer.getStyleName();
            List lSLD = sld.getStyles();
            if (sStyleName==null && lSLD.size()>0)
                sStyleName=((UserStyle_Impl)(lSLD.get(0))).getName();
            sld.setCurrentStyleName(sStyleName);
            lStyles.add(sld);
            lStyles.add(new SquareVertexStyle());
            lStyles.add(new LabelStyle());
            gpLayer.setStyles(lStyles);
        }catch(Exception e){
            e.printStackTrace();
        }
        layerManager.setFiringEvents(bFiringEvents);
    }
	
	public static void convertWMS(IACDynamicLayer acDynamicLayer,ILayerManager layerManager, FeatureDataset features) {
        try{
            String name =  (String)acDynamicLayer.getParams().get(0); //El nombre de la Categoria sera el primero de las capas WMS referenciadas
            layerManager.addCategory(name); //Se debe a que las posiciones empiezan desde 0
            DynamicLayer dynamicLayer = convertDynamic(acDynamicLayer,layerManager);
            dynamicLayer.setFeatureCollection(features);
            layerManager.addLayerable(name, dynamicLayer,acDynamicLayer.getPositionOnMap());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


	public static DynamicLayer convertDynamic(IACDynamicLayer acDynamicLayer,ILayerManager layerManager){
		
		ACDynamicLayer layer=(ACDynamicLayer)acDynamicLayer;
		
        if (layerManager!=null)
        	layer.setSrs("EPSG:"+layerManager.getCoordinateSystem().getEPSGCode());
        
        logger.info("Loading Dynamic Layer:"+layer.getUrl());
    	DynamicLayer lRet=new DynamicLayer(layer.getUrl(), layer.getSrs(), layer.getFormat(), 
    							layer.getVersion(),layer.getTime(),layer.getParams(),layer.getStyles());
        lRet.setId_LayerDataBase(layer.getId_layer());
        lRet.setSystemId(layer.getSystemName());
        lRet.setName(layer.getName());
        lRet.setActiva(layer.isActive());
        lRet.setVisible(layer.isVisible());
        lRet.setEditable(layer.isEditable());
        lRet.setFieldExtendedForm(layer.getExtendedForm());
        lRet.setRevisionActual(layer.getRevisionActual());
        lRet.setVersionable(layer.isVersionable());
        lRet.setTime(layer.getTime());
        lRet.setUltimaRevision(layer.getUltimaRevision());
        if (layerManager!=null){
            lRet.setLayerManager(layerManager);
            if (layer.getStyleXML()!=null)
                applyStyle(layer,lRet,layerManager,layer.getStyleXML());
        }
        return lRet;
    }
	
    public static void convert(ACWMSLayer acWMSLayer,ILayerManager layerManager) {
    	
        try{
//            String name =  (String)params.get(0); //El nombre de la Categoria sera el primero de las capas WMS referenciadas
        	
//        	String name = layerManager.
            layerManager.addCategory(acWMSLayer.getName()); //Se debe a que las posiciones empiezan desde 0
            WMSLayer wmsLayer=new WMSLayer(layerManager, acWMSLayer.getUrl(), 
            						acWMSLayer.getSrs(), acWMSLayer.getParams(), acWMSLayer.getFormat(), 
            						acWMSLayer.getVersion(),acWMSLayer.getStyles(), acWMSLayer.getName());
            wmsLayer.setId(acWMSLayer.getId());
            wmsLayer.setVisible(acWMSLayer.isVisible());
            layerManager.addLayerable(acWMSLayer.getName(), wmsLayer, acWMSLayer.getPosition()-1);
        }
        catch(Exception e){
        	
        	//Aunque falle al cargar la capa la añadimos al panel
        	//En algun momento funcionara.
             try {
				WMSLayer wmsLayer= new WMSLayer(layerManager, acWMSLayer.getUrl(), acWMSLayer.getSrs(), 
						acWMSLayer.getParams(), acWMSLayer.getFormat(), acWMSLayer.getVersion(),
						acWMSLayer.getStyles(), acWMSLayer.getName(),false);
				 wmsLayer.setId(acWMSLayer.getId());
				 wmsLayer.setVisible(acWMSLayer.isVisible());
				 layerManager.addLayerable(acWMSLayer.getName(), wmsLayer, acWMSLayer.getPosition()-1);
			} catch (IOException e1) {
				// TODO Auto-generated catch blockº
				e1.printStackTrace();
			}        	
             e.printStackTrace();
        }
    }
}
