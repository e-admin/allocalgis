/**
 * NetworkTools.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.waternetwork.network;

import java.awt.Component;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.server.administradorCartografia.CancelException;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.ui.plugin.waternetwork.join.JoinTools;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureUtil;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class NetworkTools{
	
	private static AppContext aplicacion = (AppContext) AppContext
	.getApplicationContext();
	
	//Carga la capa "base_red_abastecimiento" de LocalGIS y añade en ella las features 
	//editadas de la capa "capa_fusion".
	@SuppressWarnings("unchecked")
	public void loadNetworkLayer(AppContext appContext, PlugInContext context, Layer layerFusion){
		try{	
			GeopistaLayer layer = null;
			ArrayList<Exception> listaErrPerm = new ArrayList<Exception>();
			String layerName = I18N.get("WaterNetworkPlugIn","Network.Layer");		
			GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();
			Map<String, Serializable> properties = new HashMap<String, Serializable>();
			properties.put("mapadestino", (GeopistaMap) context.getTask());
			properties.put("nodofiltro", FilterLeaf.equal("1", new Integer(1)));
			try{
				properties.put("srid_destino", Integer.valueOf(context.getLayerManager()
						.getCoordinateSystem().getEPSGCode()));
			}catch (Exception e1) {							
			}

			serverDataSource.setProperties(properties);
			GeopistaConnection geopistaConnection = (GeopistaConnection) serverDataSource.getConnection();
			Collection<Exception> exceptions = new ArrayList<Exception>();
			URL urlLayer = new URL("geopistalayer://default/"+ layerName);
			geopistaConnection.executeQuery(urlLayer.toString(), exceptions, null);	

			if (exceptions.size() > 0) {
				Iterator<Exception> recorreExcepcion = exceptions.iterator();
				while (recorreExcepcion.hasNext()) {
					Exception e = (Exception) recorreExcepcion.next();
					if (e instanceof CancelException){
						JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de mapa cancelada");
					}
					else if (e
							.getCause()
							.getLocalizedMessage()
							.toString()
							.equals(
									"PermissionException: Geopista.Layer.Leer")) {
						listaErrPerm.add(e);
					} 
					else if (e instanceof CancelException){
						JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de mapa cancelada");
					}
					else {
						JOptionPane
								.showMessageDialog(
										(Component) context
												.getWorkbenchGuiComponent(),
										aplicacion
												.getI18nString("LoadSystemLayers.CapaErronea"));
					}
				}				
			}
			layer = (GeopistaLayer) geopistaConnection.getLayer();
			DataSourceQuery dataSourceQuery = new DataSourceQuery();
			dataSourceQuery.setQuery(urlLayer.toString());
			dataSourceQuery.setDataSource(serverDataSource);
			layer.setDataSourceQuery(dataSourceQuery);

			context.getLayerManager().addLayer(layer.getName(),layer);
			ArrayList<Feature> features = new ArrayList<Feature>();      
			ArrayList<Feature> aux = new ArrayList<Feature>();
	        for (Iterator<Feature> i = layerFusion.getFeatureCollectionWrapper().iterator(); i.hasNext();)
	        	features.add(i.next());
	        for(Feature f : features){
        		Feature feature = FeatureUtil.toFeature(f.getGeometry(), layer.getFeatureCollectionWrapper().getFeatureSchema());
	            if(f.getGeometry() instanceof LineString){
	            	((GeopistaFeature) feature).setLayer((GeopistaLayer) layer);	            	
	            	setAttributesFeature(feature,features);
	            	aux.add(feature); 
	            }
	        }
        	layer.getFeatureCollectionWrapper().addAll(aux);
        	
        	Collection<Layer> layers;
            layers = (Collection<Layer>) context.getWorkbenchContext().getLayerNamePanel().getLayerManager().getLayers();
            for(Layer l : layers)
            	if(l.getName().equals(I18N.get("WaterNetworkPlugIn","JoinReservoir.PipesCopy")) 
            			|| l.getName().equals(I18N.get("WaterNetworkPlugIn","JoinReservoir.AuxLayer")) 
            			|| l.getName().equals(I18N.get("WaterNetworkPlugIn","Network.FusionLayer"))
            			|| l.getName().equals(I18N.get("WaterNetworkPlugIn","JoinValve.VALVUL"))
            			|| l.getName().equals(I18N.get("WaterNetworkPlugIn","JoinPipe.ABASDI"))
            			|| l.getName().equals(I18N.get("WaterNetworkPlugIn","JoinPoliceNumber.NPLayer"))
            			|| l.getName().equals(I18N.get("WaterNetworkPlugIn","JoinReservoir.ABDECN")))
            		l.setVisible(false);
            
			if (listaErrPerm.size() > 0) {
				JOptionPane.showMessageDialog((Component) context
						.getWorkbenchGuiComponent(), (listaErrPerm.size())
						+ " capa(s) no han sido abiertas por falta de permisos");
			}
			
		}catch (Exception e){
			JOptionPane.showMessageDialog((Component) context
					.getWorkbenchGuiComponent(), aplicacion
					.getI18nString("LoadSystemLayers.CapaErronea"));
		}
	}
	
	private JoinTools join = new JoinTools();
	private GeometryFactory factory = new GeometryFactory();

	//Establece IDs a los nodos de la red. Solo se estableceran dichos IDs a los nodos iniciales
	//y los nodos finales de cada tuberia ya que Epanet no tiene en cuenta los nodos intermedios.
	public ArrayList<Point> nodesIds(ArrayList<Feature> features){
		ArrayList<Point> nodes = new ArrayList<Point>();
		ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
		for(Feature feature:features){
			coordinates.add(join.initialNode(feature));
			coordinates.add(join.finalNode(feature));
		}
		coordinates = deleteDuplicates(coordinates);
		for(Coordinate coordinate : coordinates)
			nodes.add(factory.createPoint(coordinate));
		for(int i=0;i<nodes.size();i++)
			nodes.get(i).setSRID(i+1);//se establece un unico srid a las features auxiliares para poder identificarlas a la hora de aplicar los estilos
		return nodes;
	}

	//Generera los atributos de las features que se incluiran en la capa "red_abastecimiento".
	public void setAttributesFeature (Feature feature, ArrayList<Feature> features){
		if(feature.getGeometry().getSRID()==2011){
			((GeopistaFeature) feature).setAttribute("tipo", "a");		
			((GeopistaFeature) feature).setAttribute("length", ""+feature.getGeometry().getLength());
			((GeopistaFeature) feature).setAttribute("roughness", 100);
			((GeopistaFeature) feature).setAttribute("status", "Open");			
		}
		else if(feature.getGeometry().getSRID()==2012){
			((GeopistaFeature) feature).setAttribute("tipo", "r");
		}
		else if(feature.getGeometry().getSRID()==2010){
			((GeopistaFeature) feature).setAttribute("tipo", "v");
			((GeopistaFeature) feature).setAttribute("type", "PRV");
			((GeopistaFeature) feature).setAttribute("setting", 0);
			((GeopistaFeature) feature).setAttribute("status", "Open");	
		}
		else if(feature.getGeometry().getSRID()==2009){
			((GeopistaFeature) feature).setAttribute("tipo", "h");
			((GeopistaFeature) feature).setAttribute("length", ""+feature.getGeometry().getLength());
			((GeopistaFeature) feature).setAttribute("numero_policia", ""+feature.getGeometry().getUserData());
			((GeopistaFeature) feature).setAttribute("roughness", 100);
			((GeopistaFeature) feature).setAttribute("status", "Open");
		}
		else if(feature.getGeometry().getLength()!=0.0){
			((GeopistaFeature) feature).setAttribute("tipo", "p");
			((GeopistaFeature) feature).setAttribute("length", ""+feature.getGeometry().getLength());
			((GeopistaFeature) feature).setAttribute("roughness", 100);
			((GeopistaFeature) feature).setAttribute("status", "Open");
		}
		else ((GeopistaFeature) feature).setAttribute("tipo", "x");

		ArrayList<Point> nodes = nodesIds(features);
		for(Point node : nodes){
			if(join.initialNode(feature).equals(node.getCoordinate()))
				((GeopistaFeature) feature).setAttribute("node1", node.getSRID());
			if(join.finalNode(feature).equals(node.getCoordinate()))
				((GeopistaFeature) feature).setAttribute("node2", node.getSRID());
		}		
		((GeopistaFeature) feature).setAttribute("diameter", 10);		
		((GeopistaFeature) feature).setAttribute("minorloss", 0);		
	}
	
	// Devuelve una lista de coordenadas sin los elementos duplicados.
	public ArrayList<Coordinate> deleteDuplicates(
			ArrayList<Coordinate> coordinates) {
		ArrayList<Coordinate> coordinatesAux = new ArrayList<Coordinate>();
		for (Coordinate coordinate : coordinates)
			if (!coordinatesAux.contains(coordinate))
				coordinatesAux.add(coordinate);
		return coordinatesAux;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Feature> dividePipes(Layer layer, Feature pipe, ArrayList<Feature> pipes){    
		ArrayList<Feature> aux = new ArrayList<Feature>();
		ArrayList<Feature> pipesAux = new ArrayList<Feature>(); 
		for(Iterator<Feature> i = layer.getFeatureCollectionWrapper().iterator();i.hasNext();)
			pipesAux.add(i.next());
		if(pipesAux.contains(pipe)){
			for (Feature p : pipesAux){
		        Coordinate first = pipe.getGeometry().getCoordinates()[0];
		        Coordinate last = pipe.getGeometry().getCoordinates()[pipe.getGeometry().getCoordinates().length-1];	
				Coordinate[] cs = p.getGeometry().getCoordinates();
				ArrayList<Coordinate> csA = new ArrayList<Coordinate>();
				for(Coordinate c : cs)
					csA.add(c);
				if((csA.contains(first) && (!csA.get(0).equals(first) && !csA.get(csA.size()-1).equals(first)))){					
					for(Feature p2 : join.pipeCut(p,first)){
						layer.getFeatureCollectionWrapper().add(p2);
					}
					layer.getFeatureCollectionWrapper().remove(p);
				} else if ((csA.contains(last) && (!csA.get(0).equals(last) && !csA.get(csA.size()-1).equals(last)))){					
					for(Feature p2 : join.pipeCut(p,last)){
						layer.getFeatureCollectionWrapper().add(p2);
					}
					layer.getFeatureCollectionWrapper().remove(p);
				}				
			}
		}
		return aux;
	}
}