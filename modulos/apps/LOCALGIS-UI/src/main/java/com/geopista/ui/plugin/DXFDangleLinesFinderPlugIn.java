/**
 * DXFDangleLinesFinderPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import org.deegree.graphics.sld.FeatureTypeStyle;
import org.deegree.graphics.sld.LineSymbolizer;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.Symbolizer;
import org.deegree.graphics.sld.UserStyle;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.FxccAdditionalLayers;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.style.sld.model.SLDFactory;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.util.LinearComponentExtracter;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;
import com.vividsolutions.jump.algorithm.EuclideanDistanceToPoint;
import com.vividsolutions.jump.algorithm.PointPairDistance;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDatasetFactory;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
//import com.geopista.app.catastro.intercambio.edicion.FxccAdditionalLayers;


public class DXFDangleLinesFinderPlugIn extends ThreadedBasePlugIn {
	
	private static final String SIGNIFICATIVE_FLOOR_LAYER_PREFIX = "PS";
	private static final String INTERIOR_LINE_LAYER_SUFFIX = "LI";
	private static final String PERIMETRAL_LINE_LAYER_SUFFIX = "LP";
	private static final String TOOLBAR_CATEGORY = "TopologyHealth.category";
	private static final Double DEFAULT_GAP_TOLERANCE = 0.001;
	private static final Double DEFAULT_AUTOSNAP_GAP_TOLERANCE = 0.001;
	
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
	
	private boolean nodeInputLines = true;
	private boolean useVisibleLayersOnly = false;
	private double gapTolerance = DEFAULT_GAP_TOLERANCE;
	//private double autoSnapGapTolerance = DEFAULT_AUTOSNAP_GAP_TOLERANCE;
		
	private GeometryFactory fact = new GeometryFactory();

	public DXFDangleLinesFinderPlugIn() {		
		
	}

	/**
	 * Returns a very brief description of this task.
	 * @return the name of this task
	 */
	public String getName() { 
		return "geopista.plugin.fxcc.DangleLinesFinderPlugin.name";
	}

	public EnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
		return new MultiEnableCheck()
		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
		.add(checkFactory.createAtLeastNLayersMustExistCheck(1));
	}
	
	public boolean execute(PlugInContext context) throws Exception {
		this.useVisibleLayersOnly = false;
		this.nodeInputLines = true;
		return true;
	}
	
	public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
		monitor.allowCancellationRequests();

		findDangles(context);
		
		if (monitor.isCancelRequested()){
			return;
		}
	}

	private Collection<Geometry> getLines(Collection<Feature> inputFeatures) {
		List<Geometry> linesList = new ArrayList<Geometry>();
		LinearComponentExtracter lineFilter = new LinearComponentExtracter(linesList);
		for (Feature f : inputFeatures){		
			Geometry g = f.getGeometry();
			g.apply(lineFilter);
		}
		
		return linesList;
	}

	/**
	 * Inserta nodos en las líneas que se entrecruzan, de manera que el resultado
	 * son varias líneas que comparten un mismo punto (el nodo)
	 *
	 * @param lines La coleccion de geometrias lineales a las que se quiere agregar nodos
	 * @return Una coleccion de geometrias lineales con los nodos necesarios
	 */
	private Collection<Geometry> nodeLines(Collection<Geometry> lines) {
		Geometry linesGeom = fact.createMultiLineString(GeometryFactory.toLineStringArray(lines));
		Geometry unionInput  = fact.createMultiLineString(null);
		// force the unionInput to be non-empty if possible, to ensure union is not optimized away
		Geometry point = extractPoint(lines);
		if (point != null){
			unionInput = point;
		}

		Geometry noded = linesGeom.union(unionInput);
		List<Geometry> nodedList = new ArrayList<Geometry>();
		nodedList.add((Geometry) noded);
		
		return nodedList;
	}

	private Geometry extractPoint(Collection<Geometry> lines)	{
		int minPts = Integer.MAX_VALUE;
		Geometry point = null;
		// extract first point from first non-empty geometry
		for (Geometry g : lines){		
			if (! g.isEmpty()) {
				Coordinate p = g.getCoordinate();
				point = g.getFactory().createPoint(p);
			}
		}
		
		return point;
	}
	
	/**
	 * Busca todas las lineas sueltas de un plano. Se considera una linea suelta una 
	 * linea que tiene uno de sus extremos libres en una capa concreta o en un grupo de
	 * capas tratadas como una unica entidad.
	 * 
	 * @param context
	 * @throws Exception
	 */
	private void findDangles(PlugInContext context) throws Exception{		
		List<LineString> dangleLines = new ArrayList<LineString>();		
		LayersGroup layersGroupedByFloor = groupLayersByFloor(context);
		
		for (Layer layer : layersGroupedByFloor.getIndividualLayers()){
			dangleLines.addAll(findDanglesInLayer(context, layer));
		}
		
		for (List<Layer> layersList : layersGroupedByFloor.getGroupedLayers()){
			dangleLines.addAll(findDanglesInLayerGroup(context, layersList));
		}		
		
		showDangleLines(context, dangleLines);
	}
	
	/**
	 * Agrupa las capas correspondientes a las lineas interiores y perimetrales de cada
	 * planta.
	 * 
	 * @param context
	 * @return Un objeto con los grupos de capas formados y con las capas individuales que 
	 * no pertenecen a ningún grupo
	 */
	private LayersGroup groupLayersByFloor(PlugInContext context){		
		HashMap<String, List<Layer>> layersGroupedByFloorMap = new HashMap<String,List<Layer>>();
		List<Layer> individualLayersList = new ArrayList<Layer>();
		
		List<Layer> layers = getLayers(context);
		for (Layer layer : layers){
			int separatorIndex = layer.getName().indexOf("-");
			if (separatorIndex != -1){
				String floorName = layer.getName().substring(0, separatorIndex);
				String layerType = layer.getName().substring(separatorIndex + 1);
				if (layerType.equalsIgnoreCase(PERIMETRAL_LINE_LAYER_SUFFIX)
						|| layerType.equalsIgnoreCase(INTERIOR_LINE_LAYER_SUFFIX)){
					List<Layer> floorLayers;
					if (layersGroupedByFloorMap.containsKey(floorName)){
						floorLayers = layersGroupedByFloorMap.get(floorName);										
					}
					else {
						floorLayers = new ArrayList<Layer>();
						layersGroupedByFloorMap.put(floorName, floorLayers);
					}					
					floorLayers.add(layer);					
				}
				else {
					individualLayersList.add(layer);
				}
			}
		}
		
		LayersGroup layersGroupedByFloor = new LayersGroup();
		layersGroupedByFloor.setIndividualLayers(individualLayersList);
		ArrayList<List<Layer>> layersGroupedByFloorList = new ArrayList<List<Layer>>();
		layersGroupedByFloor.setGroupedLayers(layersGroupedByFloorList);
		
		Iterator<List<Layer>> groupedLayersIterator = layersGroupedByFloorMap.values().iterator();
		while(groupedLayersIterator.hasNext()){			
			layersGroupedByFloorList.add(groupedLayersIterator.next());
		}
		
		return layersGroupedByFloor;
	}
	
	/**
	 * Busca las lineas sueltas en una capa. Se entiende como linea suelta aquella linea
	 * que tiene al menos un extremo que no esta conectado a ninguna otra linea o punto.
	 * 
	 * @param context
	 * @param layer La capa donde buscar lineas sueltas
	 * @return Una coleccion con todas las lineas sueltas encontradas
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Collection<LineString> findDanglesInLayer(PlugInContext context, Layer layer) throws Exception{		
		Collection<Feature> inputFeatures = layer.getFeatureCollectionWrapper().getFeatures();		
		Collection<LineString> dangleLines = findDanglesWithoutSnapping(inputFeatures);				
		removeFalseDangleLines(context, layer, dangleLines);
		
		return dangleLines;
	}
	
	/**
	 * Busca las lineas sueltas en un grupo de capas. Se entiende como linea suelta aquella linea
	 * que tiene al menos un extremo que no esta conectado a ninguna otra linea o punto de su capa
	 * o del resto de capas del conjunto.
	 * 
	 * @param context
	 * @param layersList Grupo de capas en las que buscar lineas sueltas
	 * @return Una coleccion con todas las lineas sueltas encontradas
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Collection<LineString> findDanglesInLayerGroup(PlugInContext context, List<Layer> layersList)
			throws Exception{		
		Collection<Feature> inputFeatures = new ArrayList<Feature>();
		for (Layer layer : layersList){
			inputFeatures.addAll(layer.getFeatureCollectionWrapper().getFeatures());
		}				
		Collection<LineString> dangleLines = findDanglesWithoutSnapping(inputFeatures);				
		removeFalseDangleLinesInLayerGroup(context, layersList, dangleLines);
		
		return dangleLines;
	}
	
	/**
	 * Busca lineas sueltas dentro de un cojunto de features.
	 * 
	 * @param inputFeatures Coleccion de features en las que buscar lineas sueltas
	 * @return Coleccion de lineas sueltas
	 */
	@SuppressWarnings("unchecked")
	private Collection<LineString> findDanglesWithoutSnapping(Collection<Feature> inputFeatures){
		Collection<Geometry> lines = getLines(inputFeatures);
		Collection<Geometry> nodedLines = lines;
		
		if (nodeInputLines) {
			//monitor.report(I18N.get("Expedientes", "geopista.plugin.fxcc.TopologyHealthPlugin.nodinglines"));
			nodedLines = nodeLines(lines);
		}
		
		Polygonizer polygonizer = new Polygonizer();
		for (Geometry line : nodedLines){		
			polygonizer.add(line);
		}
		
		Collection<LineString> dangles = polygonizer.getDangles();
		
		return dangles;
	}
	
	/**
	 * Obtiene las capas a procesar del plano, eliminando todas aquellas capas que 
	 * no se correspondan con plantas significativas.
	 * 
	 * @param context Contexto del editor de geopista en el que buscar las capas
	 * @return Capas correspondientes a las plantas significativas
	 */
	@SuppressWarnings("unchecked")
	private List<Layer> getLayers(PlugInContext context){
		List<Layer> filteredLayers = new ArrayList<Layer>();
		List<Layer> layers = context.getLayerManager().getLayers();
		for (Layer layer : layers){
			/*if (FxccAdditionalLayers.ignoreLayer(layer)){
				continue;
			}*/			
			int separatorIndex = layer.getName().indexOf("-");
			if (separatorIndex != -1){
				String floorName = layer.getName().substring(0, separatorIndex);				
				String layerType = layer.getName().substring(separatorIndex + 1);
				if (floorName.startsWith(SIGNIFICATIVE_FLOOR_LAYER_PREFIX) &&
						(layerType.equalsIgnoreCase(PERIMETRAL_LINE_LAYER_SUFFIX)
						|| layerType.equalsIgnoreCase(INTERIOR_LINE_LAYER_SUFFIX))){
					filteredLayers.add(layer);
				}
			}
		}
		
		return filteredLayers;
	}
	
	/**
	 * Devuelve falsos positivos de lineas sueltas encontrados en una capa. Se entiende como
	 * falso positivo aquellas lineas en las que uno de los extremos esta suelto, pero esta a
	 * una distancia infima de un nodo o una linea.
	 * 
	 * @param context
	 * @param layer Capa en la que buscar geometrias proximas a las lineas sueltas
	 * @param firstPassDangleLines Lineas sueltas encontradas en una pasada anterior
	 */
	private void removeFalseDangleLines(PlugInContext context, Layer layer, 
			Collection<LineString> firstPassDangleLines){
		List<Layer> layersList = new ArrayList<Layer>();
		layersList.add(layer);
		
		removeFalseDangleLinesInLayerGroup(context, layersList, firstPassDangleLines);
	}
	
	/**
	 * Devuelve falsos positivos de lineas sueltas encontrados en un grupo de capas.
	 * Se entiende como falso positivo aquellas lineas en las que uno de los extremos
	 * esta suelto, pero esta a una distancia infima de un nodo o una linea que se encuentra
	 * dentro de la geometría del conjunto de capas
	 * 
	 * @param context
	 * @param layersList Grupo de capas en la que buscar geometrias proximas a las lineas sueltas
	 * @param firstPassDangleLines Lineas sueltas encontradas en una pasada anterior
	 */
	@SuppressWarnings("unchecked")
	private void removeFalseDangleLinesInLayerGroup(PlugInContext context, List<Layer> layersList,
			Collection<LineString> firstPassDangleLines){
		
		Iterator<LineString> dangleLinesIterator = firstPassDangleLines.iterator();
		while (dangleLinesIterator.hasNext()){
			LineString dangleLine = dangleLinesIterator.next();
		
			if (dangleLine.getLength() < 0.0001){
				dangleLinesIterator.remove();
				continue;
			}
						
			Point startPoint = dangleLine.getStartPoint();
			Point endPoint = dangleLine.getEndPoint();
			Coordinate topLeftCoordinate = new Coordinate();
			topLeftCoordinate.x = startPoint.getCoordinate().x - gapTolerance;
			topLeftCoordinate.y = startPoint.getCoordinate().y + gapTolerance;
			Coordinate bottomRightCoordinate = new Coordinate();
			bottomRightCoordinate.x = startPoint.getCoordinate().x + gapTolerance;
			bottomRightCoordinate.y = startPoint.getCoordinate().y - gapTolerance;			
			Envelope startPointEnvelope = new Envelope(topLeftCoordinate, bottomRightCoordinate);		
						
			topLeftCoordinate = new Coordinate();
			topLeftCoordinate.x = endPoint.getCoordinate().x - gapTolerance;
			topLeftCoordinate.y = endPoint.getCoordinate().y + gapTolerance;
			bottomRightCoordinate = new Coordinate();
			bottomRightCoordinate.x = endPoint.getCoordinate().x + gapTolerance;
			bottomRightCoordinate.y = endPoint.getCoordinate().y - gapTolerance;
			Envelope endPointEnvelope = new Envelope(topLeftCoordinate, bottomRightCoordinate);						

			List<Feature> startPointNearFeatures = new ArrayList<Feature>();
			List<Feature> endPointNearFeatures = new ArrayList<Feature>();
			for (Layer layer : layersList){
				startPointNearFeatures.addAll(layer.getFeatureCollectionWrapper().query(startPointEnvelope));
				endPointNearFeatures.addAll(layer.getFeatureCollectionWrapper().query(endPointEnvelope));
			}
			
			if (!isDanglePoint(startPoint, startPointNearFeatures)
					&&	!isDanglePoint(endPoint, endPointNearFeatures)){

				dangleLinesIterator.remove();
				continue;
			}
		}
	}
	
	/**
	 * Comprueba que un punto extremo de una linea es realmente un punto suelto,
	 * es decir que no esta conectado a ningun otro punto o extremo de linea.
	 * Para ello se obtiene la distancia del punto respecto a otras lineas o 
	 * extremos de linea cercanos y se comprueba si dicha distancia esta por
	 * debajo de una tolerancia.
	 * 
	 * @param point Punto a verificar
	 * @param nearFeatures Features cercanas al punto
	 * @return
	 */
	private boolean isDanglePoint(Point point, Collection<Feature> nearFeatures){
		// We need to find at least to lineStrings near the point,
		// One lineString being the point owner
		// and the other one being the lineString which the previous lineString
		// is connected to.
		int nearLinesFound = 0;
		for(Feature nearFeature: nearFeatures){
			Collection<LineString> featureLines = getFeatureLines(nearFeature);			
			for (LineString lineString : featureLines){
				PointPairDistance pointPairDistance = new PointPairDistance();
				Coordinate coordinate = point.getCoordinate();
				EuclideanDistanceToPoint.computeDistance(lineString, coordinate, pointPairDistance);
				if (pointPairDistance.getDistance() < gapTolerance){
					nearLinesFound++;
				}
			}			
		}
		
		if (nearLinesFound >= 2){
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * Descompone un feature en lineas
	 * @param feature Feature a descomponer
	 * @return La coleccion de lineas que componen la feature
	 */
	private Collection<LineString> getFeatureLines(Feature feature){
		List<LineString> lineStringsList = new ArrayList<LineString>();
				
		LinearComponentExtracter lineFilter = new LinearComponentExtracter(lineStringsList);		
		Geometry g = feature.getGeometry();
		g.apply(lineFilter);
				
		return lineStringsList;
	}
	
//  Las siguientes lineas comentadas son codigo para hacer auto-snap de lineas sueltas (no funciona)
//	private void tryToSnapDangleLines(PlugInContext context, Collection<Feature> inputFeatures, 
//			Collection<LineString> firstPassDangleLines){
//				
//		List<Layer> layers = getLayers(context);
//		for (LineString dangleLine : firstPassDangleLines){
//			if (dangleLine.getLength() < 0.0001){
//				continue;
//			}
//			Feature parentFeature = findParentFeature(dangleLine, inputFeatures);
//			if (parentFeature == null){
//				continue;
//			}
//			
//			Point startPoint = dangleLine.getStartPoint();
//			Point endPoint = dangleLine.getEndPoint();
//			Coordinate topLeftCoordinate = new Coordinate();
//			topLeftCoordinate.x = startPoint.getCoordinate().x - 0.001;
//			topLeftCoordinate.y = startPoint.getCoordinate().y + 0.001;
//			Coordinate bottomRightCoordinate = new Coordinate();
//			bottomRightCoordinate.x = startPoint.getCoordinate().x + 0.001;
//			bottomRightCoordinate.y = startPoint.getCoordinate().y - 0.001;			
//			Envelope startPointEnvelope = new Envelope(topLeftCoordinate, bottomRightCoordinate);		
//						
//			topLeftCoordinate = new Coordinate();
//			topLeftCoordinate.x = endPoint.getCoordinate().x - 0.001;
//			topLeftCoordinate.y = endPoint.getCoordinate().y + 0.001;
//			bottomRightCoordinate = new Coordinate();
//			bottomRightCoordinate.x = endPoint.getCoordinate().x + 0.001;
//			bottomRightCoordinate.y = endPoint.getCoordinate().y - 0.001;
//			Envelope endPointEnvelope = new Envelope(topLeftCoordinate, bottomRightCoordinate);
//						
//			for (Layer layer : layers){
//				if(FxccAdditionalLayers.ignoreLayer(layer)){
//					continue;
//				}
//				
//				Coordinate coordinateTarget = null;
//				List<Feature> startPointNearFeatures = layer.getFeatureCollectionWrapper().query(startPointEnvelope);
//				startPointNearFeatures.remove(parentFeature);
//				coordinateTarget = snapPointToNearFeature(startPoint, startPointNearFeatures);
//				if (coordinateTarget != null){
//					moveFeatureCoordinate(parentFeature, startPoint.getCoordinate(), coordinateTarget);
//				}
//								
//				List<Feature> endPointNearFeatures = layer.getFeatureCollectionWrapper().query(endPointEnvelope);
//				endPointNearFeatures.remove(parentFeature);
//				coordinateTarget = snapPointToNearFeature(endPoint, endPointNearFeatures);				
//				if (coordinateTarget != null){
//					moveFeatureCoordinate(parentFeature, endPoint.getCoordinate(), coordinateTarget);
//				}
//			}
//		}
//	}
//	
//	private Feature findParentFeature(LineString dangleLine, Collection<Feature> features){
//		PrecisionModel pm = new PrecisionModel(10000);
//		GeometryFactory gf = new GeometryFactory(pm);
//		for(Feature feature : features){			
//			Geometry featureGeometry = gf.createGeometry(feature.getGeometry());
//			Geometry dangleLineGeometry = gf.createGeometry(dangleLine);
//			IntersectionMatrix intersectionMatrix = featureGeometry.relate(dangleLineGeometry);
//			if (intersectionMatrix.isContains() || intersectionMatrix.isWithin()){
//				return feature;
//			}
//		}
//		
//		return null;
//	}
//	
//	private Integer findCoordinateIndexInFeature(Point pointToFind, Feature feature){
//		Geometry featureGeometry = feature.getGeometry();
//		Coordinate[] coordinates = featureGeometry.getCoordinates();
//		Coordinate pointToFindCoordinate = pointToFind.getCoordinate();
//		for (int i = 0; i < coordinates.length; i++){
//			if (pointToFindCoordinate.equals2D(coordinates[i])){
//				return i;
//			}
//		}
//		
//		return null;
//	}
//	
//	private Coordinate snapPointToNearFeature(Point pointToSnap, Collection<Feature> snapToCandidates){
//		Feature snapToTarget = null;		
//		Double distanceToTarget = null;
//		Coordinate coordinateTarget = null;
//		for(Feature snapToCandidate: snapToCandidates){			
//			Geometry snapToGeometry = snapToCandidate.getGeometry();			
//			IntersectionMatrix intersectionMatrix = snapToGeometry.relate(pointToSnap);
//			if (intersectionMatrix.isContains()){
//				break;
//			}
//			else {
//				PointPairDistance pointPairDistance = new PointPairDistance();
//				Coordinate coordinate = pointToSnap.getCoordinate();
//				EuclideanDistanceToPoint.computeDistance(snapToGeometry, coordinate, pointPairDistance);
//				if (distanceToTarget == null || distanceToTarget > pointPairDistance.getDistance()){
//					distanceToTarget = pointPairDistance.getDistance();					
//					snapToTarget = snapToCandidate;
//					if (pointPairDistance.getCoordinate(0).equals2D(coordinate)){
//						coordinateTarget = pointPairDistance.getCoordinate(1);
//					}
//					else {
//						coordinateTarget = pointPairDistance.getCoordinate(0);
//					}
//				}						
//			}
//		}
//		
//		return coordinateTarget;			
//	}
//	
//	private void moveFeatureCoordinate(Feature feature, final Coordinate source, final Coordinate target){
//		Geometry featureGeometry = feature.getGeometry();
//		CoordinateFilter moveFilter = new CoordinateFilter(){
//			public void filter(Coordinate paramCoordinate) {				
//				if (paramCoordinate.equals2D(source)){
//					paramCoordinate.x = target.x;
//					paramCoordinate.y = target.y;
//				}
//			}			
//		};
//		featureGeometry.apply(moveFilter);
//		feature.setGeometry(featureGeometry);
//	}
	
	private void showDangleLines(PlugInContext context, Collection<LineString> dangleLines) throws Exception {		
		
		Layer dangleLayer = context.getLayerManager().getLayer(FxccAdditionalLayers.ISSUES_LAYER_DANGLE);
		if (dangleLayer != null){
			context.getLayerManager().remove(dangleLayer);
		}

		FeatureCollection dangleFC = FeatureDatasetFactory.createFromGeometry(dangleLines);		

		if (dangleFC.size() > 0) {
			Layer layer = context.addLayer(						
					FxccAdditionalLayers.LAYER_CATEGORY_TOPO_ISSUES,
					FxccAdditionalLayers.ISSUES_LAYER_DANGLE,
					dangleFC);			
			changeLayerStyle(layer);
			layer.setDescription(AppContext.getApplicationContext().getI18nString(
					"geopista.plugin.fxcc.DangleLinesFinderPlugin.danglingedges"));
		}
	}
	
	private void changeLayerStyle(Layer layer){
		SLDStyle sldStyle = (SLDStyle) layer.getStyle(SLDStyle.class);
		if (sldStyle == null){
			sldStyle = SLDFactory.createDefaultSLDStyle(layer.getName());
		}
		
		String styleName = sldStyle.getCurrentStyleName();
		if (styleName == null){
			return;
		}
		
		// Cogemos la primera regla que afecte a las lineas y la cambiamos
		UserStyle userStyle = sldStyle.getUserStyle(styleName);				
		FeatureTypeStyle[] featureStyles = userStyle.getFeatureTypeStyles();		
		for (int k = 0; k < featureStyles.length; k++){
			Rule[] rules = featureStyles[k].getRules();
			for (int i = 0; i < rules.length; i++){
				Symbolizer[] symbolizers = rules[i].getSymbolizers();
				for (int j = 0; j < symbolizers.length; j++){
					if (symbolizers[j] instanceof LineSymbolizer){
						LineSymbolizer lineSymbolizer = (LineSymbolizer) symbolizers[j];
						lineSymbolizer.getStroke().setStroke(Color.red);
						lineSymbolizer.getStroke().setWidth(2.5);
						return;
					}
				}
			}		
		}
	}
	
	public void initialize(PlugInContext context) throws Exception {
		
		String pluginCategory = aplicacion.getString(TOOLBAR_CATEGORY);
		((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).setTaskMonitorManager(new TaskMonitorManager());
		((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(
				getIcon(), this,
				createEnableCheck(context.getWorkbenchContext()),
				context.getWorkbenchContext());
	}

	public ImageIcon getIcon() {
		return IconLoader.icon("danglefinder.gif");
	}
	
	private class LayersGroup {
		
		private List<List<Layer>> groupedLayers;
		
		private List<Layer> individualLayers;
		
		public LayersGroup(){
			groupedLayers = new ArrayList<List<Layer>>();
			individualLayers = new ArrayList<Layer>();
		}

		public List<List<Layer>> getGroupedLayers() {
			return groupedLayers;
		}

		public void setGroupedLayers(List<List<Layer>> groupedLayers) {
			this.groupedLayers = groupedLayers;
		}

		public List<Layer> getIndividualLayers() {
			return individualLayers;
		}

		public void setIndividualLayers(List<Layer> individualLayers) {
			this.individualLayers = individualLayers;
		}

	}
}

