package com.geopista.ui.dialogs.eiel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.geopista.app.eiel.ConstantesLocalGISEIEL_LCGIII;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.dialogs.global.Utils;
import com.geopista.ui.dialogs.global.SvgToBeans;

/**
 * Clase con utilidades para parsear los svg cuadrícula y sacar la info
 * relevante a beans
 * 
 * @author irodriguez
 * 
 */
public class EIELSvgToBeans extends SvgToBeans{

	private final static Logger logger = Logger
			.getLogger(EIELSvgToBeans.class);

	/*
	 *  EIEL
	 */

	/**
	 * Indica si entre las capas desconectadas se encuentran datos EIEL
	 * 
	 * @param allExtractLayers
	 * @return
	 */
	public boolean existenEIEL(ArrayList<String> extractLayersNames) {
		return Utils.existen(extractLayersNames, Constants.TIPOS_EIEL);
	}	

	/**
	 * Indica si entre las capas desconectadas se encuentran datos EIEL
	 * 
	 * @param allExtractLayers
	 * @return
	 */
	public boolean esEIEL(String layerName) {
		return Utils.isInArray(Constants.TIPOS_EIEL, layerName);
	}	

	/**
	 * Indica si entre las capas desconectadas se encuentran Carreteras EIEL
	 * (eiel_t_carreteras)
	 * 
	 * @param allExtractLayers
	 * @return
	 */
	public boolean existenTramosCarreterasEIEL(ArrayList<String> extractLayersNames) {
		return Utils.existen(extractLayersNames, ConstantesLocalGISEIEL_LCGIII.CARRETERA_LAYER);
	}
	
	/**
	 * Indica si entre las capas desconectadas se encuentran Carreteras EIEL
	 * (eiel_t_abast_ca)
	 * 
	 * @param allExtractLayers
	 * @return
	 */
	public boolean existenCaptacionesEIEL(ArrayList<String> extractLayersNames) {
		return Utils.existen(extractLayersNames, ConstantesLocalGISEIEL_LCGIII.CAPTACIONES_LAYER);
	}
		
	
	/*
	 *  UTILIDADES
	 */	
	
	/**
	 * Dado una lista de ficheros SVG los parsea, eliminando eiel
	 * repetidas, para finalmente devolver un map cuyas claves serán los nombres
	 * de los ficheros generados y cuyos valores seran listas de eiel
	 * 
	 * @param ficherosSVG
	 * @return
	 */
	public Map<String, List<EIELMetadataSVG>> parseEIEL(
			List<File> ficherosSVG) {
		Map<String, List<EIELMetadataSVG>> hashEIELTotal = new HashMap<String, List<EIELMetadataSVG>>();
		List<EIELMetadataSVG> listEIEL = null;
		EIELMetadataSVG eiel = null;

		// recorro todos los ficheros sacando los tipos de eiel
		// desconectadas
		for (int i = 0; i < ficherosSVG.size(); i++) {
			listEIEL = parseEIELSvg(ficherosSVG.get(i));
			for (Iterator iterator = listEIEL.iterator(); iterator
					.hasNext();) {
				eiel = (EIELMetadataSVG) iterator.next();
				updateMapListEIEL(hashEIELTotal,
						eiel.getEIELFileName(), eiel);
			}
		}

		return hashEIELTotal;
	}

	/**
	 * Parsea un fichero SVG para obtener las eiel existentes. Clave:
	 * nombreFicher, Valor:listaeiel
	 * 
	 * @param numFichLic
	 * @param file
	 * @return
	 */
	private List<EIELMetadataSVG> parseEIELSvg(File file) {
		/**
		 * Algoritmo: 1) Recorro el fichero por <g> 2) Busco el id y lo comparo
		 * con las capas de eiel 3) En caso de ser eiel
		 * automaticamente creo el fichero e inserto las features
		 * correspondientes
		 * 
		 */
		List<EIELMetadataSVG> listRet = new ArrayList<EIELMetadataSVG>();
		SAXBuilder builder = new SAXBuilder();
		try {
			String numCelda = file.getName().replace(".svg", "");
			Document doc = builder.build(file);
			Element rootElement = doc.getRootElement();
			String encabezadoRawText = getRawNodeTextOpened(rootElement);
			logger.debug(encabezadoRawText);
			List children = rootElement.getChildren("g");
			List childFeat = null;
			Element elemLayer = null;
			String layerSystemId = null;
			String grupoRawText = null;
			EIELMetadataSVG eiel = null;
			String pathRawText = null;
			String idFeatStr = null;
			HashMap<String,String> idAttributes = null;
			HashMap<String, Element> eielNoDuplicadas = null; 
			Element elemFeat = null;
			Attribute att = null;
			for (int i = 0; i < children.size(); i++) {
				Object child = children.get(i);
				if (child instanceof Element) {
					elemLayer = (Element) child;
					layerSystemId = elemLayer.getAttributeValue("systemId");
					// logger.debug("Capa " + layerSystemId);
					boolean isEIEL = Utils.isInArray(Constants.TIPOS_EIEL,
							layerSystemId);
					// capa de eiel o inventario
					if (isEIEL) {
						grupoRawText = getRawNodeTextOpened(elemLayer);
						logger.debug(grupoRawText);
					
						childFeat = elemLayer.getChildren();
						if (childFeat.size() > 0) {
							eielNoDuplicadas = new HashMap<String, Element>();
							for (Iterator iterator = childFeat.iterator(); iterator
									.hasNext();) {
								elemFeat = (Element) iterator.next();
								pathRawText = getRawNodeTextClosed(elemFeat);
								logger.debug(pathRawText);
								idAttributes = getPkAttributes(elemLayer.getAttributes(), elemFeat);
									eiel = new EIELMetadataSVG(
											encabezadoRawText, grupoRawText,
											pathRawText, numCelda,
											layerSystemId, idAttributes);
									listRet.add(eiel);							
							}
						}
						else{
							pathRawText = getEmptyPath(elemLayer.getAttributes());	
							eiel = new EIELMetadataSVG(
									encabezadoRawText, grupoRawText,
									pathRawText, numCelda,
									layerSystemId, idAttributes);
							listRet.add(eiel);	
						}							
					}
				}
			}

			// sobreescribimos el fichero
			XMLOutputter outputter = new XMLOutputter();
			outputter.output(doc, new FileOutputStream(file));

		} catch (JDOMException e) {
			logger.error(e, e);
		} catch (IOException e) {
			logger.error(e, e);
		}

		return listRet;
	}
	
	private HashMap<String,String> getPkAttributes(List featAttributes, Element elemFeat){
		HashMap<String,String> idAttributes = new HashMap<String,String>();
		Attribute att = null;
		int i=1;
		for (int j = 0; j < featAttributes.size(); j++) {
			att = (Attribute) featAttributes.get(j);
			if(att.getName().toLowerCase().contains("l" + i)){
				//System.out.println(att.getValue() + "=" + elemFeat.getAttribute("v" + i).getValue());
				idAttributes.put(att.getValue(), elemFeat.getAttribute("v" + i).getValue());	
				i++;
			}
		}	
		return idAttributes;
	}
	
	private String getEmptyPath(List featAttributes){
		String emptyPath = "<path #attributes# d=\"\">";
		Attribute att = null;
		int i=1;
		String attributes = "";
		for (int j = 0; j < featAttributes.size(); j++) {
			att = (Attribute) featAttributes.get(j);
			if(att.getName().toLowerCase().contains("l" + i)){
				attributes += "v" + i + "=\"-1\" ";	
				i++; 
			}
		}	
		return emptyPath.replace("#attributes#", attributes);
	}
	
	/**
	 * Añade un objeto al map dentro de una lista como valores
	 * 
	 * @param map
	 * @param o
	 */
	private void updateMapListEIEL(Map map, String key, EIELMetadataSVG value) {
		List<EIELMetadataSVG> list = null;
		if (map.containsKey(key)) {
			list = (List<EIELMetadataSVG>) map.get(key);
		} else {
			list = new ArrayList<EIELMetadataSVG>();
		}

		// para no meter eiel duplicadas
//		EIELMetadataSVG eielAux = null;
		
//		Iterator it = list.iterator();		
//		while(it.hasNext()){
//			EIELMetadataSVG eielMetadataSVG = (EIELMetadataSVG) it.next();
//			
//		for (int i = 0; i < list.size(); i++) {
//			eielAux = list.get(i);
//			if (eielAux.getIdFeatStr().equals(value.getIdFeatStr())) {
//				return;
//			}
//		}
		
//		}

		list.add(value);
		map.put(key, list);
	}

}
