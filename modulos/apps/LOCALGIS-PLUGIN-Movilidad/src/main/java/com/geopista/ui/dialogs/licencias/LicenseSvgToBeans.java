/**
 * LicenseSvgToBeans.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.licencias;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.dialogs.global.SvgToBeans;
import com.geopista.ui.dialogs.global.Utils;

/**
 * Clase con utilidades para parsear los svg cuadrícula y sacar la info relevante a beans
 * @author irodriguez
 *
 */
public class LicenseSvgToBeans extends SvgToBeans{
	
	private final static Logger logger = Logger.getLogger(LicenseSvgToBeans.class);

	
	/*
	 * LICENCIAS
	 */
	
	/**
	 * Indica si entre las capas desconectadas se encuentra alguna de licencias
	 * @param allExtractLayers
	 * @return
	 */
	public boolean existenLicencias(ArrayList<String> extractLayersNames) {
		return Utils.existen(extractLayersNames, Constants.TIPOS_LICENCIAS);
	}

	/**
	 * Indica si entre las capas desconectadas se encuentran licencias de obra mayor
	 * @param allExtractLayers
	 * @return
	 */
	public boolean existenLicObraMayor(ArrayList<String> extractLayersNames) {
		return Utils.existen(extractLayersNames, Constants.LIC_OBRA_MAYOR);
	}
	
	/**
	 * Indica si entre las capas desconectadas se encuentran licencias de obra menor
	 * @param allExtractLayers
	 * @return
	 */
	public boolean existenLicObraMenor(ArrayList<String> extractLayersNames) {
		return Utils.existen(extractLayersNames, Constants.LIC_OBRA_MENOR);
	}
	
	/**
	 * Indica si entre las capas desconectadas se encuentran licencias de activida
	 * @param allExtractLayers
	 * @return
	 */
	public boolean existenLicActividad(ArrayList<String> extractLayersNames) {
		return Utils.existen(extractLayersNames, Constants.LIC_ACTIVIDAD);
	}
	
	
	/*
	 * ACTIVIDADES CONTAMINANTES
	 */
	
	/**
	 * Indica si entre las capas desconectadas se encuentran inventario de patrimonio
	 * @param allExtractLayers
	 * @return
	 */
	public boolean existenTiposActividadesContaminantes(ArrayList<String> extractLayersNames) {
		return Utils.existen(extractLayersNames, Constants.TIPOS_CONTAMINANTES);
	}

	public boolean existenActividadesContaminantes(ArrayList<String> extractLayersNames) {
		return Utils.existen(extractLayersNames, Constants.ACTIVIDADES_CONTAMINANTES);		
	}
	
	public boolean existenArboledas(ArrayList<String> extractLayersNames) {
		return Utils.existen(extractLayersNames, Constants.ARBOLEDA);	
	}
	
	public boolean existenVertederos(ArrayList<String> extractLayersNames) {
		return Utils.existen(extractLayersNames, Constants.VERTEDERO);	
	}	
	
	
	/*
	 * INVENTARIO DE PATRIMONIO 
	 */
	
	/**
	 * Indica si entre las capas desconectadas se encuentran inventario de patrimonio
	 * @param allExtractLayers
	 * @return
	 */
	public boolean existenInvPatrimonio(ArrayList<String> extractLayersNames) {
		return Utils.existen(extractLayersNames, Constants.TIPOS_INVENTARIO);
	}
	
	/**
	 * Indica si entre las capas desconectadas se encuentran inventario de parcelas
	 * @param allExtractLayers
	 * @return
	 */
	public boolean existenInvParcelas(ArrayList<String> extractLayersNames) {
		return Utils.existen(extractLayersNames, Constants.INV_PARCELAS);		
	}
	
	
	/**
	 * Indica si entre las capas desconectadas se encuentran inventario de vias
	 * @param allExtractLayers
	 * @return
	 */
	public boolean existenInvVias(ArrayList<String> extractLayersNames) {
		return Utils.existen(extractLayersNames, Constants.INV_VIAS);
	}
	
	/*
	 * UTILIDADES 
	 */
	
	/**
	 * Dado una lista de ficheros SVG los parsea, eliminando licencias repetidas, para finalmente devolver
	 * un map cuyas claves serán los nombres de los ficheros generados y cuyos valores seran listas de licencias
	 * @param ficherosSVG
	 * @return
	 */
	public Map<String, List<LicenseMetadataSVG>> parseLicencias(List<File> ficherosSVG, int numFichLic){
		Map<String, List<LicenseMetadataSVG>> hashLicenciasTotal = new HashMap<String, List<LicenseMetadataSVG>>();
		List<LicenseMetadataSVG> listLicencias = null;
		LicenseMetadataSVG licencia = null;
		
		//recorro todos los ficheros sacando los tipos de licencias desconectadas
		for (int i = 0; i < ficherosSVG.size(); i++) {
			listLicencias = parseLicenciaSvg(ficherosSVG.get(i), numFichLic);
			for (Iterator iterator = listLicencias.iterator(); iterator.hasNext();) {
				licencia = (LicenseMetadataSVG) iterator.next();
				updateMapListLicense(hashLicenciasTotal, licencia.getLicenseFileName(), licencia);
			}
		}
		
		return hashLicenciasTotal;
	}
			
	/**
	 * Parsea un fichero SVG para obtener las licencias existentes. Clave: nombreFicher, Valor:listalicencias
	 * @param numFichLic 
	 * @param file 
	 * @return
	 */
	private List<LicenseMetadataSVG> parseLicenciaSvg(File file, int numFichLic) {
		/**
		 * Algoritmo:
		 * 1) Recorro el fichero por <g>
		 * 2) Busco el id y lo comparo con las capas de licencias
		 * 3) En caso de ser licencias automaticamente creo el fichero e inserto las features correspondientes 
		 * 
		 */
		List<LicenseMetadataSVG> listRet = new ArrayList<LicenseMetadataSVG>();
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
			LicenseMetadataSVG license = null;
			String pathRawText = null;
			String idFeatStr = null;
			HashMap<String, Element> licenciasNoDuplicadas = null; //clave: refCatastral, valor:licenciaNodo
			Element elemFeat = null;
			String attRefCat = null;
			String attRefCatValue = null;
			String attNumBienes = null;
			String attNumBienesValue = null;
			Attribute att = null;
			for (int i = 0; i < children.size(); i++) {
				Object child = children.get(i);
				if(child instanceof Element){
					elemLayer = (Element)child;
					layerSystemId = elemLayer.getAttributeValue("systemId");
					//logger.debug("Capa " + layerSystemId);
					boolean isLicencias = Utils.isInArray(Constants.TIPOS_LICENCIAS, layerSystemId);
					boolean isInventario = Utils.isInArray(Constants.TIPOS_INVENTARIO, layerSystemId);
					boolean isContaminantes = Utils.isInArray(Constants.TIPOS_CONTAMINANTES, layerSystemId);
					//capa de licencias o inventario
					if(isLicencias || isInventario||isContaminantes){
						grupoRawText = getRawNodeTextOpened(elemLayer);
						logger.debug(grupoRawText);
						//buscamos el atributo clave: Referencia Catastral o ref_catastral o lo que sea
						List featAttributes = elemLayer.getAttributes();
						for (int j = 0; j < featAttributes.size(); j++) {
							att = (Attribute) featAttributes.get(j);
							if(att.getValue().toLowerCase().contains(Constants.CLAVE_METAINFO)){
								attRefCat = "v"+Integer.parseInt(att.getName().substring(1));
								break;
							}
						}
						//Problema con los acentos. 
						attNumBienes = getAttributeFeature(elemLayer.getAttributes(), Constants.NUM_BIENES);
						if (attNumBienes==null)
							attNumBienes = getAttributeFeature(elemLayer.getAttributes(), Constants.NUM_BIENES_2);
						childFeat = elemLayer.getChildren();
						if(childFeat.size()>0){
							licenciasNoDuplicadas = new HashMap<String, Element>();
							for (Iterator iterator = childFeat.iterator(); iterator.hasNext(); ) {
								elemFeat = (Element) iterator.next();
								attRefCatValue = elemFeat.getAttributeValue(attRefCat);
								attNumBienesValue = elemFeat.getAttributeValue(attNumBienes);
								pathRawText = getRawNodeTextClosed(elemFeat);
								logger.debug(pathRawText);
								idFeatStr = elemFeat.getAttributeValue("v1");
								if(isLicencias){
									license = new LicenseMetadataSVG(encabezadoRawText, grupoRawText, pathRawText, numCelda, idFeatStr, layerSystemId, numFichLic, attRefCatValue);
									listRet.add(license);
									//guardamos unicamente las features no duplicadas
									if(!licenciasNoDuplicadas.containsKey(attRefCatValue));{
										licenciasNoDuplicadas.put(attRefCatValue, elemFeat);
									}
								}
								/*else if(isInventario && Integer.parseInt(attNumBienesValue) > 0){
									license = new MetadataSVG(encabezadoRawText, grupoRawText, pathRawText, numCelda, idFeatStr, layerSystemId, numFichLic, attRefCatValue);
									listRet.add(license);
								}*/
								else if(isInventario){
									license = new LicenseMetadataSVG(encabezadoRawText, grupoRawText, pathRawText, numCelda, idFeatStr, layerSystemId, numFichLic, attRefCatValue);
									listRet.add(license);
								}
								else if(isContaminantes){
									license = new LicenseMetadataSVG(encabezadoRawText, grupoRawText, pathRawText, numCelda, idFeatStr, layerSystemId, numFichLic, attRefCatValue);
									listRet.add(license);
								}
							}

							//borramos las licencias duplicadas, es decir, si coincide su referencia catastral
							if(isLicencias){
								elemLayer.removeContent();
								elemLayer.addContent(licenciasNoDuplicadas.values());
							}
						}
						attRefCat = null;
					}
				}
			}
			
			//sobreescribimos el fichero
		    XMLOutputter outputter = new XMLOutputter();
		    outputter.output(doc, new FileOutputStream(file));       
			
		} catch (JDOMException e) {
			logger.error(e,e);
		} catch (IOException e) {
			logger.error(e,e);
		}
	    
		return listRet;
	}
	
	/**
	 * Añade un objeto al map dentro de una lista como valores
	 * @param map
	 * @param o
	 */
	private void updateMapListLicense(Map map, String key, LicenseMetadataSVG value){
		List<LicenseMetadataSVG> list = null;
		if(map.containsKey(key)){
			list = (List<LicenseMetadataSVG>) map.get(key);
		}
		else {
			list = new ArrayList<LicenseMetadataSVG>();
		}

		//para no meter licencias duplicadas
		LicenseMetadataSVG licAux = null;
		for (int i = 0; i < list.size(); i++) {
			licAux = list.get(i);
			if(licAux.getIdFeatStr().equals(value.getIdFeatStr())){
				return ;
			}
		}
		
		list.add(value);
		map.put(key, list);
	}
	
	public static void main(String[] args) {
		try {
			String locale = "es_ES";
			LicenseSvgToBeans svgToBeansUtils = new LicenseSvgToBeans();
			LicenseDBAccessUtils dbAccessUtils = new LicenseDBAccessUtils();
			LicenseFilesUtils licenseFilesUtils = new LicenseFilesUtils(locale);
			File dirBase = new File("C:\\Archivos de programa\\LocalGIS\\webapps\\localgismobile\\");
			File file1 = new File(dirBase, "13.svg");
			File file2 = new File(dirBase, "10.svg");
			//File file3 = new File(dirBase, "3.svg");
			File file4 = new File(dirBase, "14.svg");
			File file5 = new File(dirBase, "7.svg");
			File file6 = new File(dirBase, "9.svg");
			List<File> listFiles = new ArrayList<File>();
			listFiles.add(file1);
			listFiles.add(file2);
			//listFiles.add(file3);
			listFiles.add(file4);
			listFiles.add(file5);
			listFiles.add(file6);
			int numFichLic = 5;
			//parseo de los svg cuadrícula
			Map<String, List<LicenseMetadataSVG>> hashLicencias = svgToBeansUtils.parseLicencias(listFiles, numFichLic);
			
			//muestreo de info
			Iterator<String> keyIt = hashLicencias.keySet().iterator();
			while (keyIt.hasNext()) {
				String ficheroLicencias = (String) keyIt.next();
				System.out.println("<" + ficheroLicencias + ">"); 
				List<LicenseMetadataSVG> listLicencias = hashLicencias.get(ficheroLicencias);
				for (Iterator iterator = listLicencias.iterator(); iterator.hasNext();) {
					LicenseMetadataSVG licenciasSVG = (LicenseMetadataSVG) iterator.next();
					System.out.println("\t" + licenciasSVG.getPath());
				}
			}
			
//			//creación de ficheros de licencias
//			List<File> licensesFiles = licenseFilesUtils.createLicensesFilesSkeleton(hashLicencias, dirBase, numFichLic);
//			//añadimos info sobre sacada de BBDD
//			dbAccessUtils.addDataBaseInfoLicenses(hashLicencias, "http://localhost:8081/licencias", locale);
//			//rellenamos los ficheros de licencias
//			licenseFilesUtils.fillLicenseFiles(hashLicencias, dirBase);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
