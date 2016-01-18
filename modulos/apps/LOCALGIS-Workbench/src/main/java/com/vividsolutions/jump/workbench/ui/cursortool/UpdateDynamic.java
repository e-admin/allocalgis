
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

package com.vividsolutions.jump.workbench.ui.cursortool;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.deegree.graphics.sld.CssParameter;
import org.deegree_impl.graphics.sld.FeatureTypeStyle_Impl;
import org.deegree_impl.graphics.sld.LineSymbolizer_Impl;
import org.deegree_impl.graphics.sld.Mark_Impl;
import org.deegree_impl.graphics.sld.PointSymbolizer_Impl;
import org.deegree_impl.graphics.sld.PolygonSymbolizer_Impl;
import org.deegree_impl.graphics.sld.Stroke_Impl;
import org.deegree_impl.graphics.sld.Symbolizer_Impl;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.io.datasource.IGeopistaConnection;
import com.geopista.model.DynamicLayer;
import com.geopista.model.GeopistaLayer;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.Reprojector;
import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;

/**
 * Con este plugin el usuario dibuja un recuadro sobre una capa dinámica. En el interior de este recuadro se permitirá al usuario
 * visualizar las features de esa capa que están almacenadas en la base de datos.
 * @author miriamperez
 *
 */
public class UpdateDynamic {
	LayerViewPanel panel;
	Layer layer = null;
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    public UpdateDynamic(LayerViewPanel panel) {
    	this.panel = panel;
    }


    protected TaskComponent getTaskFrame() {
        return (TaskComponent) SwingUtilities.getAncestorOfClass(TaskComponent.class,
           (JComponent) panel);
    }

    public void getFeatures(Geometry geom){
    	getTaskFrame().getLayerViewPanel().getSelectionManager().clear();
        GeopistaLayer layer = null;
        Layer[] layers = getTaskFrame().getLayerNamePanel().getSelectedLayers();
        int n = layers.length;
        int i = 0;
        for(i=0;i<n;i++){
        	layer = (GeopistaLayer)layers[i];
        	if (layer instanceof DynamicLayer){
        		this.layer = layer;
        		break;
        	}
        }
        if (i==n){
        	i = 0;
        	List listLayers = panel.getLayerManager().getLayers();
        	n = listLayers.size();
            for(i=0;i<n;i++){
            	layer = (GeopistaLayer)listLayers.get(i);
            	if (layer instanceof DynamicLayer){
            		this.layer = layer;
            		break;
            	}
            }
        }
        LayerManager layerManager = (LayerManager)layer.getLayerManager();
        boolean firingEvents = layerManager.isFiringEvents();
        layerManager.setFiringEvents(false);
    	try{
	        if (i<n){
		    	CoordinateSystem inCoord = layer.getLayerManager().getCoordinateSystem();
		        geom.setSRID(inCoord.getEPSGCode());
		        IGeopistaConnection geopistaConnection = (IGeopistaConnection) layer.getDataSourceQuery().getDataSource().getConnection();
		        DriverProperties driverProperties = geopistaConnection.getDriverProperties();
		        driverProperties.put("filtrogeometrico",geom);
	        	driverProperties.put("srid_destino",inCoord.getEPSGCode());
		        geopistaConnection.setDriverProperties(driverProperties);

		        //Creamos una coleccion para almacenar las excepciones que se producen
		        Collection exceptions = new ArrayList();
		        //Miro en qué srid por defecto se almacenan las features en la base de datos
		        String sridDefecto = geopistaConnection.getSRIDDefecto(true, -1);
		    	CoordinateSystem outCoord = PredefinedCoordinateSystems.getCoordinateSystem(Integer.parseInt(sridDefecto));
	            if (inCoord.getEPSGCode() != outCoord.getEPSGCode()){
	            	Reprojector.instance().reproject(geom,inCoord, outCoord);
	            }
	            DataSourceQuery dataSourceQuery = layer.getDataSourceQuery();
	            if (layer.isEditable())
	            	layer.setRevisionActual(-1);
                layer.setDinamica(false);
				FeatureCollection featureCollection = geopistaConnection.executeQueryLayer(layer.getDataSourceQuery().getQuery(),exceptions,null,(GeopistaLayer)layer);
				layer.setFeatureCollection(featureCollection);
				resaltarCapa(layer);

                layer.setDinamica(false);
	        }else{
	        	getTaskFrame().getLayerViewPanel().getContext().warnUser(
	            		I18N.get("NingunaCapaDinamica"));
	        }
        }catch(Exception e){
            ErrorDialog.show(
                    (Component) ((WorkbenchGuiComponent) SwingUtilities.getAncestorOfClass(WorkbenchGuiComponent.class,
                            panel)),
                    aplicacion
                            .getI18nString("GeopistaLoadMapPlugIn.CapaErronea"),
                    e
                            .getCause()
                            .getMessage(),
                    StringUtil
                            .stackTrace(e));
        }finally{
        	layerManager.setFiringEvents(firingEvents);
            ((SLDStyleImpl)((GeopistaLayer)layer).getStyle(SLDStyleImpl.class)).setFillColor(Color.RED);
            ((SLDStyleImpl)((GeopistaLayer)layer).getStyle(SLDStyleImpl.class)).setLineColor(Color.RED);
        }

    }

    public EnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
	    EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
	    return new MultiEnableCheck()
	        .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
	        .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1))
	        .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1));
    }

    /**
     * Resalta la parte vectorial que se ha cargado de la DynamicLayer en un color más oscuro
     * @param layer
     */
    private void resaltarCapa(GeopistaLayer layer){
		SLDStyleImpl sldStyle = (SLDStyleImpl)layer.getStyle(SLDStyleImpl.class);
		FeatureTypeStyle_Impl featureTypeStyle = (FeatureTypeStyle_Impl)sldStyle.getDefaultStyle().getFeatureTypeStyles()[0];
		Symbolizer_Impl symbolizer = (Symbolizer_Impl)featureTypeStyle.getRules()[0].getSymbolizers()[0];
		String codigoColor = "";
		Color color = Color.WHITE;
		if (symbolizer instanceof PolygonSymbolizer_Impl){
			codigoColor = (String)((CssParameter)((PolygonSymbolizer_Impl) symbolizer).getFill().getCssParameters().get("fill")).getValue().getComponents()[0];
			color = new Color(Integer.parseInt(codigoColor.substring(1),16));
		}
		else if (symbolizer instanceof LineSymbolizer_Impl){
			codigoColor = (String)((CssParameter)((LineSymbolizer_Impl) symbolizer).getStroke().getCssParameters().get("stroke")).getValue().getComponents()[0];
			color = new Color(Integer.parseInt(codigoColor.substring(1),16));
		}
		else if (symbolizer instanceof PointSymbolizer_Impl){
			color = ((Stroke_Impl)((Mark_Impl)((PointSymbolizer_Impl) symbolizer).getGraphic().getMarksAndExtGraphics()[0]).getStroke()).getColor();
		}
		BasicStyle style = new BasicStyle(color.darker());
		layer.addStyle(style);

    }

}
