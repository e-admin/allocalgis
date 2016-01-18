package com.geopista.ui.plugin.waternetwork.join;

import java.util.*;

import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.GeopistaAddNewLayerPlugIn;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.*;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.ui.GeometryEditor;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

public class JoinTools {

	private GeometryFactory factory = new GeometryFactory();
	private GeometryEditor geometryEditor = new GeometryEditor();
	public Coordinate initialNode(Feature feature){return feature.getGeometry().getCoordinates()[0];}
	public Coordinate finalNode(Feature feature){
		return feature.getGeometry().getCoordinates()[feature.getGeometry().getCoordinates().length - 1];
	}
	
	//Devuelve un array con los vertices de tuberias que no pertenecen a otras tuberias, es decir,
	//que no están unidos a otras tuberias.
	public ArrayList<Coordinate> disconnectedNodes(ArrayList<Feature> pipes){
		ArrayList<Feature> aux = pipes;
		ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
		for(Feature pipe : pipes){
			int contI = 0;
			int contF = 0;
			for(Feature a : aux){
				if(!a.equals(pipe)){
					ArrayList<Coordinate> coo = new ArrayList<Coordinate>();
					for(Coordinate c : a.getGeometry().getCoordinates())
						coo.add(c);
					if(coo.contains(initialNode(pipe)))
						contI++;
					if(coo.contains(finalNode(pipe)))
						contF++;
				}
			}
			if(contI==0)coordinates.add(initialNode(pipe));
			if(contF==0)coordinates.add(finalNode(pipe));
		}
		return coordinates;
	}
	
	//Devuelve un array con las features que conforman la division de una tuberia.
	//Es necesario para poder calcular la ruta correctamente desde un punto de la 
	//red al embalse
	public ArrayList<Feature> pipeCut(Feature pipe, Coordinate coordinate){
		ArrayList<Feature> newFeatures = new ArrayList<Feature>();
		//Pasamos las coordenadas de la feature aux a un array para poder divirlo en 3
		Coordinate[] coordAux = pipe.getGeometry().getCoordinates();
		ArrayList<Coordinate> arrayAux = new ArrayList<Coordinate>();
		for(int i=0;i<coordAux.length;i++){
			if(coordAux[i].x==coordinate.x)
				coordAux[i].y=coordinate.y;
			else if(coordAux[i].y==coordinate.y)
				coordAux[i].x=coordinate.x;
			arrayAux.add(coordAux[i]);
		}
		
		//Generamos la primera tuberia		
		Coordinate[] initialF = null;
		if(arrayAux.indexOf(coordinate)==0){
			initialF = new Coordinate[2];
			initialF[0]=coordinate;
			initialF[1]=coordinate;
		}
		else{
			initialF = new Coordinate[arrayAux.indexOf(coordinate)+1];
			for(int x=0;x<arrayAux.indexOf(coordinate)+1;x++)		
				initialF[x]=arrayAux.get(x);
		}
		Geometry initialFGeometry = factory.createLineString(initialF);
		Feature initialFFeature = FeatureUtil.toFeature(initialFGeometry, pipe.getSchema());
		newFeatures.add(initialFFeature);
		
		//Generamos la ultima tuberia
		Coordinate[] finalF = new Coordinate[(arrayAux.size())-arrayAux.indexOf(coordinate)];
		int cont = 0;
		for(int y=arrayAux.indexOf(coordinate);y<arrayAux.size();y++){
			finalF[cont]=arrayAux.get(y);
			cont++;
		}
		Geometry finalFGeometry = factory.createLineString(finalF);
		Feature finalFFeature = FeatureUtil.toFeature(finalFGeometry, pipe.getSchema());
		newFeatures.add(finalFFeature);
		
		return newFeatures;
	}

	// Genera tuberias auxliares que corrigen los posibles errores en el diseño
	// de la red de abastecimiento.
	@SuppressWarnings("unchecked")
	public void newPipes(LayerViewPanel layerViewPanel, Layer copy, Layer layer, ArrayList<Feature> pipes, double distance){
		ArrayList<Coordinate> disconnectedNodes = disconnectedNodes(pipes);
		ArrayList<Coordinate> copyDN = (ArrayList<Coordinate>) disconnectedNodes.clone();
		for(Coordinate node : disconnectedNodes){					
			if(copyDN.contains(node)){
				Point nodePoint = factory.createPoint(node);
				Feature pipe = pipeNear(pipes,distance,nodePoint,"");
				Feature remove = null;
				ArrayList<Feature> addNF = new ArrayList<Feature>();			
				if(pipe!=null){				
					Geometry geometryPipe = pipe.getGeometry();
					double distanceBuffer = nodePoint.distance(geometryPipe);
					Geometry buffer = nodePoint.buffer(distanceBuffer+1);	
					if(buffer.intersects(pipe.getGeometry())){					
						Coordinate coordinateIntersects = buffer.intersection(geometryPipe).getCoordinates()[0];
						Geometry geometryPipe2 = geometryEditor.insertVertex(geometryPipe, coordinateIntersects, geometryPipe.getEnvelope());		
						Feature pipe2 = FeatureUtil.toFeature(geometryPipe2, pipe.getSchema());
						for(Feature p : pipeCut(pipe2,coordinateIntersects)){
							((GeopistaFeature) p).setLayer((GeopistaLayer) copy);
							copy.getFeatureCollectionWrapper().add(p);
							addNF.add(p);
						}
						copy.getFeatureCollectionWrapper().remove(pipe);					
						Coordinate[] coordinates = {node,coordinateIntersects};
						Geometry geometryAux = factory.createLineString(coordinates);
						Feature pipeAux = FeatureUtil.toFeature(geometryAux, layer.getFeatureCollectionWrapper().getFeatureSchema());
						((GeopistaFeature) pipeAux).setLayer((GeopistaLayer) layer);
						pipeAux.getGeometry().setSRID(2011);
						layer.getFeatureCollectionWrapper().add(pipeAux);					
						layerViewPanel.getSelectionManager().getFeatureSelection().selectItems(layer, pipeAux);					
						remove=pipe;
						if(copyDN.contains(node))copyDN.remove(node);
						if(copyDN.contains(coordinateIntersects))copyDN.remove(coordinateIntersects);
					}				
				}	
				pipes.addAll(addNF);	
				pipes.remove(remove);	
			}
		}		
	}

	// Devuelve la tuberia más cercana a un punto y a una distacia determinadas.
	public Feature pipeNear(ArrayList<Feature> pipes, double distance,
			Point nodePoint, String type) {
		for (Feature pipe : pipes) {
			ArrayList<Coordinate> cts = new ArrayList<Coordinate>();
			for (Coordinate c : pipe.getGeometry().getCoordinates())
				cts.add(c);
			if (nodePoint.distance(pipe.getGeometry()) <= distance
					&& !cts.contains(nodePoint.getCoordinate()))
				return pipe;
			else if (!type.equals("")
					&& cts.contains(nodePoint.getCoordinate()))
				return pipe;
		}
		return null;
	}

	// Acota el numero de elementos que se deben incluir en la red de
	// abastecimiento a aquellos que se encuentren
	// a una distancia máxima de una tuberia.
	public void opt(LayerViewPanel layerViewPanel, Layer layer,
			ArrayList<Feature> pipes, ArrayList<Feature> elements, String type) {
		ArrayList<Feature> opt = new ArrayList<Feature>();
		for (Feature element : elements) {
			Point elem = factory.createPoint(element.getGeometry()
					.getCoordinate());
			double distance = distanceMin(elem, pipes);
			if (distance <= 20)
				opt.add(element);
		}
		newElements(layerViewPanel, layer, pipes, opt, type);
	}

	// Incluye en la red de abastecimiento nuevos elementos, ya sean valvulas o
	// numeros de policia.
	public ArrayList<Feature> newElements(LayerViewPanel layerViewPanel,
			Layer layer, ArrayList<Feature> pipes, ArrayList<Feature> elements,
			String type) {
		for (Feature element : elements) {
			Point elem = factory.createPoint(element.getGeometry()
					.getCoordinate());
			ArrayList<Feature> list = new ArrayList<Feature>();
			double distance = distanceMin(elem, pipes);
			Feature pipe = pipeNear(pipes, distance, elem, type);
			if (pipe != null && pipe.getGeometry().getSRID()!=2010 && pipe.getGeometry().getSRID()!=2009) {
				Geometry buffer = elem.buffer(distance + 0.1);
				if (buffer.intersects(pipe.getGeometry())) {
					Geometry coordianteIntrsect = buffer.intersection(pipe
							.getGeometry());
					Geometry pipeGeom = pipe.getGeometry();
					ArrayList<Coordinate> pG = new ArrayList<Coordinate>();
					for (Coordinate c : pipeGeom.getCoordinates()) {
						pG.add(c);
					}
					Geometry pipeGeom2 = geometryEditor.insertVertex(pipeGeom,
							coordianteIntrsect.getCoordinates()[0],
							pipeGeom.getEnvelope());
					int i = coordianteIntrsect.getCoordinates().length - 1;
					Geometry pipeGeom3 = geometryEditor.insertVertex(pipeGeom2,
							coordianteIntrsect.getCoordinates()[i],
							pipeGeom2.getEnvelope());
					Coordinate e = null;
					ArrayList<Coordinate> pG3 = new ArrayList<Coordinate>();
					for (Coordinate c : pipeGeom3.getCoordinates())
						pG3.add(c);
					for (Coordinate c : pipeGeom3.getCoordinates()) {
						if (!pG.contains(c)) {
							e = c;
							break;
						}
					}
					if (e == null) {
						Feature elemF = FeatureUtil.toFeature(pipeGeom,
								pipe.getSchema());
						((GeopistaFeature) elemF)
								.setLayer((GeopistaLayer) layer);
						layer.getFeatureCollectionWrapper().add(elemF);
						layerViewPanel.getSelectionManager()
								.getFeatureSelection()
								.selectItems(layer, elemF);
						layer.getFeatureCollectionWrapper().remove(pipe);
						layer.getFeatureCollectionWrapper().remove(element);
					} else {
						ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
						int next = 0;
						for (int q = pG3.indexOf(e); q < (pG3.indexOf(e) + coordianteIntrsect
								.getCoordinates().length); q++) {
							if (q != pG3.size())
								coordinates.add(pG3.get(q));
							else {
								next = 1;
								break;
							}
						}
						if (next != 0)
							break;
						int w = coordinates.size() - 1;
						int pos = -1;
						for (Coordinate c : coordinates)
							if (pG.contains(c)) {
								pos = coordinates.indexOf(c);
								break;
							}
						int cont2 = -1;
						if (pos != 0) {
							Coordinate[] first = new Coordinate[pG3
									.indexOf(coordinates.get(0)) + 1];
							for (int x = 0; x < pG3.indexOf(coordinates.get(0)) + 1; x++) {
								cont2++;
								first[cont2] = pG3.get(x);
							}
							if (first.length < 2)
								break;
							Geometry firstG = factory.createLineString(first);
							Feature firstF = FeatureUtil.toFeature(firstG,
									pipe.getSchema());
							list.add(firstF);
						}

						Coordinate[] el = new Coordinate[coordinates.size()];
						cont2 = -1;
						for (int q = pG3.indexOf(coordinates.get(0)); q < (pG3
								.indexOf(coordinates.get(0)) + coordinates
								.size()); q++) {
							cont2++;
							el[cont2] = pG3.get(q);
						}
						if (el.length < 2)
							break;
						Geometry elG = factory.createLineString(el);
						Feature elF = FeatureUtil.toFeature(elG,
								pipe.getSchema());
						if (type.equals("valve"))
							elF.getGeometry().setSRID(2010);
						else if (type.equals("house")){
							elF.getGeometry().setSRID(2009);
							try{
								elF.getGeometry().setUserData(element.getAttribute("Rótulo"));
							}catch(Exception er){
								elF.getGeometry().setUserData(" ");
							}
						}
						list.add(elF);

						if (pos != w) {
							Coordinate[] last = new Coordinate[pG3.size()
									- pG3.indexOf(coordinates.get(w))];
							cont2 = -1;
							for (int q = (pG3.indexOf(coordinates.get(w))); q < pG3
									.size(); q++) {
								cont2++;
								last[cont2] = pG3.get(q);
							}
							if (last.length < 2)
								break;
							Geometry lastG = factory.createLineString(last);
							Feature lastF = FeatureUtil.toFeature(lastG,
									pipe.getSchema());
							list.add(lastF);
						}
						for (Feature li : list) {
							((GeopistaFeature) li)
									.setLayer((GeopistaLayer) layer);
							layer.getFeatureCollectionWrapper().add(li);
							if (li.getGeometry().getSRID() == 2010
									|| li.getGeometry().getSRID() == 2009)
								layerViewPanel.getSelectionManager()
										.getFeatureSelection()
										.selectItems(layer, li);
						}
						layer.getFeatureCollectionWrapper().remove(pipe);
						layer.getFeatureCollectionWrapper().remove(element);
					}
				} else {
					System.out.println("Error en elemento: " + element.getID());
				}
				pipes.remove(pipe);
				pipes.addAll(list);
				list.clear();
			}
		}
		return pipes;
	}

	// Devuelve la distancia mínima de un elemento tipo Point a una tuberia.
	public double distanceMin(Point point, ArrayList<Feature> features) {
		double distanceM = point.distance(features.get(0).getGeometry());
		for (int i = 1; i < features.size(); i++)
			if (point.distance(features.get(i).getGeometry()) <= distanceM)
				distanceM = point.distance(features.get(i).getGeometry());
		return distanceM;
	}

	// Genera tuberias auxiliares que unen los embalses a la red de
	// abastecimiento.
	@SuppressWarnings("unchecked")
	public void newPipesReservoir(Layer layer, LayerViewPanel layerViewPanel,
			ArrayList<Feature> pipes, Layer reservoirLayer) {
		FeatureCollection featureCollection = reservoirLayer
				.getFeatureCollectionWrapper();
		ArrayList<Feature> reservoirs = new ArrayList<Feature>();
		for (Iterator<Feature> i = featureCollection.iterator(); i.hasNext();)
			reservoirs.add(i.next());
		for (Feature embalse : reservoirs) {
			Point e = factory
					.createPoint(embalse.getGeometry().getCoordinate());
			Feature pipe = pipeNear(pipes, distanceMin(e, pipes), e, "");
			Geometry pipeGeom = pipe.getGeometry();
			ArrayList<Coordinate> pipeC = new ArrayList<Coordinate>();
			for (Coordinate c : pipeGeom.getCoordinates())
				pipeC.add(c);
			double distance = e.distance(pipeGeom) + 5;
			Feature pipeAux = pipe;
			ArrayList<Feature> list = new ArrayList<Feature>();
			if (e.buffer(distance).intersects(pipeGeom)) {
				Coordinate cPipe = null;
				if (e.buffer(distance).contains(
						factory.createPoint(pipeGeom.getCoordinates()[0])))
					cPipe = pipeGeom.getCoordinates()[0];
				else if (e.buffer(distance).contains(
						factory.createPoint(pipeGeom.getCoordinates()[pipeGeom
								.getCoordinates().length - 1])))
					cPipe = pipeGeom.getCoordinates()[pipeGeom.getCoordinates().length - 1];
				else {
					cPipe = e.buffer(distance).intersection(pipeGeom)
							.getCoordinate();
					pipeGeom = geometryEditor.insertVertex(pipeGeom, cPipe,
							pipeGeom.getEnvelope());
					for (Coordinate c : pipeGeom.getCoordinates()) {
						if (!pipeC.contains(c)) {
							cPipe = c;
							break;
						}
					}
					pipeAux.setGeometry(pipeGeom);
				}
				Coordinate[] coord = { e.getCoordinate(), cPipe };
				Geometry aux = factory.createLineString(coord);
				Feature aux2 = FeatureUtil.toFeature(aux, pipe.getSchema());
				aux2.getGeometry().setSRID(2011);
				list.add(aux2);
				Coordinate[] cReservoir = { e.getCoordinate(),
						e.getCoordinate() };
				Feature fReservoir = FeatureUtil.toFeature(
						factory.createLineString(cReservoir), pipe.getSchema());
				((GeopistaFeature) fReservoir).setLayer((GeopistaLayer) layer);
				fReservoir.getGeometry().setSRID(2012);
				list.add(fReservoir);
				for (Feature l : list) {
					((GeopistaFeature) l).setLayer((GeopistaLayer) layer);
					layer.getFeatureCollectionWrapper().add(l);
					layerViewPanel.getSelectionManager().getFeatureSelection()
							.selectItems(layer, l);
				}
			}
			pipes.remove(pipe);
			pipes.add(pipeAux);
		}
	}

	// Devuelve una capa donde se fusionarán los elementos de la capa de
	// válvulas y la capa
	// de tuberías.
	public Layer fusionLayer(LayerViewPanel layerViewPanel,
			ArrayList<Feature> features) {
		Layer newLayer = layerViewPanel.getLayerManager().addLayer(
				StandardCategoryNames.WORKING,
				I18N.get("WaterNetworkPlugIn", "Network.FusionLayer"),
				GeopistaAddNewLayerPlugIn.createBlankFeatureCollection());
		newLayer.setVisible(true);
		newLayer.setEditable(true);
		newLayer.getFeatureCollectionWrapper().addAll(features);
		return newLayer;
	}
}