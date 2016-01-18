package com.geopista.app.inventario;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.deegree.xml.XMLTools;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import com.geopista.app.metadatos.xml.XMLTranslator_LCGIII;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.inventario.BienBean;

public class InventarioUtil {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(InventarioUtil.class);

	/**
	 * Georreferenciacion por referencia catastral.
	 * 
	 * @param bienBean
	 * @param datosGeorreferenciacion
	 * @return
	 */
	public static boolean georreferenciacionPorReferenciaCatastral(
			GeopistaEditor geopistaEditor, BienBean bienBean,
			Element datosGeorreferenciacion) {

		boolean encontrado = false;

		if (datosGeorreferenciacion != null) {
			NodeList elementosRef = XMLTranslator_LCGIII.recuperarHijos(
					datosGeorreferenciacion, "ref_catastral");
			if (elementosRef != null) {
				Collection<GeopistaFeature> features = new ArrayList();
				for (int i = 0; i < elementosRef.getLength(); i++) {
					Element element = (Element) elementosRef.item(i);
					String refCatastral = XMLTools.getValue(element);
					bienBean.setRefCatastralOrigen(refCatastral);
					if ((refCatastral != null) && (!refCatastral.equals(""))) {
						logger.info("Referencia Catastral obtenida:"+ refCatastral);
						//Con que exista el atributo lo marcamos como georreferenciable
						//aunque luego no exista la referencia
						encontrado = true;
						Collection<GeopistaFeature> featuresObtenidas = getFeaturesByRefCatastral(
								geopistaEditor, refCatastral);
						if (featuresObtenidas != null){							
							bienBean.setTipoGeorreferenciacion(BienBean.GEO_CATASTRAL);
						}
						if (featuresObtenidas!=null){
							//Verificamos que las features no se han insertado ya previamente.						
							Iterator<GeopistaFeature> it=featuresObtenidas.iterator();
							while (it.hasNext()){
								GeopistaFeature geopistaFeature=it.next();
								if (!features.contains(geopistaFeature)){
									features.add(geopistaFeature);
								}
								else{
									logger.info("Ya existe la feature para la referencia catastral:"+refCatastral);
								}
							}							
							//features.addAll(featuresObtenidas);
						}
					}
				}
				bienBean.setIdFeatures(features);
			}
		}
		return encontrado;
	}

	/**
	 * Georreferenciacion por id de via
	 * 
	 * @param bienBean
	 * @param datosGeorreferenciacion
	 * @return
	 */

	public static boolean georreferenciacionPorVia(
			GeopistaEditor geopistaEditor, BienBean bienBean,
			Element datosGeorreferenciacion) {

		boolean encontrado = false;

		if (datosGeorreferenciacion != null) {
			NodeList elementosRef = XMLTranslator_LCGIII.recuperarHijos(
					datosGeorreferenciacion, "calle_numero");

			if (elementosRef != null) {

				Collection<GeopistaFeature> features = new ArrayList();
				for (int i = 0; i < elementosRef.getLength(); i++) {

					Element element = (Element) elementosRef.item(i);

					String idViaIne = "";
					String idEntidadColectivaIne = "";
					String idEntidadSingularIne = "";
					String numeroPolicia = "";
					Element elementidViaIne = XMLTranslator_LCGIII.recuperarHijo(
							element, "idViaIne");
					Element elementidEntidadSingularIne = XMLTranslator_LCGIII
							.recuperarHijo(element, "idEntidadSingularIne");
					Element elementidEntidadColectivaIne = XMLTranslator_LCGIII
					.recuperarHijo(element, "idEntidadColectivaIne");
					Element elementNumeroPolicia = XMLTranslator_LCGIII.recuperarHijo(
							element, "numeroPolicia");
					if (elementidViaIne != null)
						idViaIne = XMLTools.getValue(elementidViaIne);
					if (elementidEntidadSingularIne != null)
						idEntidadSingularIne = XMLTools
								.getValue(elementidEntidadSingularIne);
					if (elementidEntidadColectivaIne != null)
						idEntidadColectivaIne = XMLTools
								.getValue(elementidEntidadColectivaIne);
					if (elementNumeroPolicia != null)
						numeroPolicia = XMLTools.getValue(elementNumeroPolicia);

					if ((idViaIne != null) && (!idViaIne.equals(""))
							&& (idEntidadSingularIne != null)
							&& (!idEntidadSingularIne.equals(""))
							&& (idEntidadColectivaIne != null)
							&& (!idEntidadColectivaIne.equals(""))
							&& (numeroPolicia != null)
							&& (!numeroPolicia.equals(""))) {
						logger.info("Id Via Ine obtenida:" + idViaIne);
						logger.info("Id Entidad Singular obtenida:"	+ idEntidadSingularIne);
						logger.info("Id Entidad Colectiva obtenida:"	+ idEntidadSingularIne);
						logger.info("Numero Policia obtenido:" + numeroPolicia);

						//Con que exista el atributo lo marcamos como georreferenciable
						//aunque luego no exista la referencia
						encontrado = true;

						Collection<GeopistaFeature> featuresObtenidas = getFeaturesByVia(
								geopistaEditor, idViaIne,
								idEntidadSingularIne,numeroPolicia);
						if (featuresObtenidas != null){
							bienBean.setTipoGeorreferenciacion(BienBean.GEO_VIA);
						}
						if (featuresObtenidas!=null){
							Iterator<GeopistaFeature> it=featuresObtenidas.iterator();
							while (it.hasNext()){
								GeopistaFeature geopistaFeature=it.next();
								if (!features.contains(geopistaFeature)){
									features.add(geopistaFeature);
								}
								else{
									logger.info("Ya existe la feature para la via:"+idViaIne);
								}
							}		
						
						//features.addAll(featuresObtenidas);
						}
					}
					else{
						logger.error("No existe información completa sobre la georreferenciación por via");
					}
				}
				bienBean.setIdFeatures(features);
			}
		}
		return encontrado;
	}

	/**
	 * Georreferenciacion por coordenadas XY
	 * 
	 * @param geopistaEditor
	 * @param bienBean
	 * @param datosGeorreferenciacion
	 * @return
	 */
	public static boolean georreferenciacionPorXY(
			GeopistaEditor geopistaEditor, BienBean bienBean,
			Element datosGeorreferenciacion) {

		boolean encontrado = false;

		if (datosGeorreferenciacion != null) {
			NodeList elementosRef = XMLTranslator_LCGIII.recuperarHijos(
					datosGeorreferenciacion, "coordenadas");

			if (elementosRef != null) {

				Collection<GeopistaFeature> features = new ArrayList();
				for (int i = 0; i < elementosRef.getLength(); i++) {

					Element element = (Element) elementosRef.item(i);

					String sistemaReferencia = "";
					String coordenadasXY = "";
					Element elementCoordenadasXY = XMLTranslator_LCGIII.recuperarHijo(
							element, "coordenadasXY");
					Element elementSistemaReferencia = XMLTranslator_LCGIII
							.recuperarHijo(element, "sistemaReferencia");
					if (elementSistemaReferencia != null)
						sistemaReferencia = XMLTools.getValue(elementSistemaReferencia);
					if (elementCoordenadasXY != null)
						coordenadasXY = XMLTools.getValue(elementCoordenadasXY);

					if ((coordenadasXY != null) && (!coordenadasXY.equals(""))) {
						logger.info("Coordenada XY obtenida:" + coordenadasXY);
						
						//Con que exista el atributo lo marcamos como georreferenciable
						//aunque luego no exista la referencia
						encontrado = true;

						Collection<GeopistaFeature> featuresObtenidas = getFeaturesByXY(
								geopistaEditor, coordenadasXY,sistemaReferencia);
						if (featuresObtenidas != null){
							bienBean.setTipoGeorreferenciacion(BienBean.GEO_XY);
						}
						if (featuresObtenidas!=null){
							Iterator<GeopistaFeature> it=featuresObtenidas.iterator();
							while (it.hasNext()){
								GeopistaFeature geopistaFeature=it.next();
								if (!features.contains(geopistaFeature)){
									features.add(geopistaFeature);
								}
								else{
									logger.info("Ya existe la feature para la x,y:"+coordenadasXY);
								}
							}	
							//features.addAll(featuresObtenidas);
						}
					}
				}
				bienBean.setIdFeatures(features);
			}
		}
		return encontrado;
	}

	/**
	 * Georreferenciacion clasica por Bien Inventario
	 * 
	 * @param geopistaEditor
	 * @param bienBean
	 * @param datos
	 * @return
	 */
	public static boolean georreferenciacionPorBienInventario(
			GeopistaEditor geopistaEditor, BienBean bienBean, Element datos) {

		boolean encontrado = false;

		Element elementoRef = XMLTranslator_LCGIII.recuperarHijo(datos,
				"ref_catastral");
		
		Collection<GeopistaFeature> features = new ArrayList();
		if (elementoRef != null) {
			String refCatastral = XMLTools.getValue(elementoRef);
			bienBean.setRefCatastralOrigen(refCatastral);
			if (refCatastral != null) {
				Collection<GeopistaFeature> featuresObtenidas = getFeaturesByRefCatastral(geopistaEditor, refCatastral);
				
				
				if (featuresObtenidas != null){							
					bienBean.setTipoGeorreferenciacion(BienBean.GEO_CATASTRAL);
				}
				if (featuresObtenidas!=null){
					//Verificamos que las features no se han insertado ya previamente.						
					Iterator<GeopistaFeature> it=featuresObtenidas.iterator();
					while (it.hasNext()){
						GeopistaFeature geopistaFeature=it.next();
						if (!features.contains(geopistaFeature)){
							features.add(geopistaFeature);
						}
						else{
							logger.info("Ya existe la feature para la referencia catastral Caso 2:"+refCatastral);
						}
					}							
					//features.addAll(featuresObtenidas);
				}
				
				
				
				bienBean.setIdFeatures(features);
				bienBean.setTipoGeorreferenciacion(BienBean.GEO_CATASTRAL);

			}
		}
		return encontrado;
	}

	/**
	 * Obtiene las features asociadas a una referencia catastral
	 * 
	 * @param refCatastral
	 * @return
	 */
	static Collection<GeopistaFeature> getFeaturesByRefCatastral(
			GeopistaEditor geopistaEditor, String refCatastral) {
		if (refCatastral.length() > 14) {
			refCatastral = refCatastral.substring(0, 14);
			logger.debug("Referencia catastral demasiado grande transformada a:"
					+ refCatastral);
		}

		Collection<GeopistaFeature> features = searchByAttribute(geopistaEditor, "inventario_parcelas",2, refCatastral);
		return features;
	}

	/**
	 * Obtiene las features por coordenadas.
	 * @param geopistaEditor
	 * @param coordenadaXY
	 * @param epsg
	 * @return
	 */
	static Collection<GeopistaFeature> getFeaturesByXY(
			GeopistaEditor geopistaEditor, String coordenadaXY, String epsg) {

		Collection<GeopistaFeature> features = null;
		try {
			String[] listaCoordenadas = coordenadaXY.split(",");
			float x = Float.parseFloat(listaCoordenadas[0]);
			float y = Float.parseFloat(listaCoordenadas[1]);

			features = searchByContains(geopistaEditor, x, y,
					Integer.parseInt(epsg));
		} catch (Exception e) {
			logger.error("Coordenadas x,y incorrectas:" + coordenadaXY);
		}
		return features;
	}

	static Collection<GeopistaFeature> getFeaturesByVia(
			GeopistaEditor geopistaEditor, String idViaIne,
			String idEntidadSingularIne, String numeroPolicia) {

		Collection<GeopistaFeature> features = null;
		try {

			 features = searchByAttribute(geopistaEditor,"inventario_vias",3,idViaIne);
		} catch (Exception e) {

		}
		return features;
	}

	/**
	 * Busca feature por un id de atributo
	 * 
	 * @param geopistaLayer
	 * @param attributeNumber
	 * @param value
	 * @return
	 */
	static private Collection<GeopistaFeature> searchByAttribute(
		GeopistaEditor geopistaEditor, String layername,int attributeNumber, String value) {
		GeopistaLayer layerBuscada = null;
		if (geopistaEditor == null)
			return null;
		java.util.List<GeopistaLayer> layers = geopistaEditor
				.getLayerViewPanel().getLayerManager().getLayers();
		for (int i = 0; i < layers.size(); i++) {
			GeopistaLayer layer = (GeopistaLayer) layers.get(i);
			if (layer.getSystemId().equals(layername)) {
				layerBuscada = layer;
				break;
			}

		}
		Collection<GeopistaFeature> finalFeatures = new ArrayList<GeopistaFeature>();
		if (layerBuscada == null)
			return finalFeatures;
		java.util.List allFeaturesList = layerBuscada
				.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext()) {
			GeopistaFeature localFeature = (GeopistaFeature) allFeaturesListIter
					.next();
			String nombreAtributo = localFeature.getString(attributeNumber).trim();
			if (nombreAtributo.equals(value)) {
				finalFeatures.add(localFeature);
			}
		}

		return finalFeatures;
	}
	
	

	static private Collection<GeopistaFeature> searchByContains(
			GeopistaEditor geopistaEditor, float x, float y, int epsg) {
		GeopistaLayer layerParcelas = null;
		if (geopistaEditor == null)
			return null;
		java.util.List<GeopistaLayer> layers = geopistaEditor
				.getLayerViewPanel().getLayerManager().getLayers();
		for (int i = 0; i < layers.size(); i++) {
			GeopistaLayer layer = (GeopistaLayer) layers.get(i);
			if (layer.getSystemId().equals("inventario_parcelas")) {
				layerParcelas = layer;
				break;
			}

		}
		Collection<GeopistaFeature> finalFeatures = new ArrayList<GeopistaFeature>();
		if (layerParcelas == null)
			return finalFeatures;
		java.util.List allFeaturesList = layerParcelas.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext()) {
			GeopistaFeature localFeature = (GeopistaFeature) allFeaturesListIter.next();

			GeometryFactory factory = new GeometryFactory(new PrecisionModel(),epsg);
			Point puntoRef = factory.createPoint(new Coordinate(x, y));
			if (puntoRef.coveredBy(localFeature.getGeometry())) {
				String nombreAtributo = localFeature.getString(2).trim();
				logger.info("Localizado bien x,y en parcela con refcatastral:"
						+ nombreAtributo);
				finalFeatures.add(localFeature);
			}

		}

		return finalFeatures;
	}

}
