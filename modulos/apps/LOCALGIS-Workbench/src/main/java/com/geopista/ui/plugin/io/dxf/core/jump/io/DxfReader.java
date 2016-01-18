/**
 * DxfReader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Creado el 17-abr-2004
 *
 * Este codigo se distribuye bajo licencia GPL
 * de GNU. Para obtener una cópia integra de esta
 * licencia acude a www.gnu.org.
 * 
 * Este software se distribuye "como es". AGIL
 * solo  pretende desarrollar herramientas para
 * la promoción del GIS Libre.
 * AGIL no se responsabiliza de las perdidas económicas o de 
 * información derivadas del uso de este software.
 */
package com.geopista.ui.plugin.io.dxf.core.jump.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.JUMPReader;

/**
 * Clase encargada de cargar FeatureCollection en memoria a partir
 * de la información geográfica definida en un fichero Dxf.
 *
 * @author Alvaro Zabala (AGIL)
 */
public class DxfReader implements JUMPReader {


	public DxfReader() {
	}

	public FeatureCollection read(DriverProperties dp) throws Exception {

		String dxfPath = (String) dp.get("File");
		if (dxfPath==null)
			dxfPath = (String) dp.get("Original File");
		//System.out.println("dxfPath: "+dxfPath);
		return createFeatureCollectionFromSelection(dxfPath);
	}
	
	public FeatureCollection readSpecial(DriverProperties dp) throws Exception {

		String dxfPath = (String) dp.get("File");
		if (dxfPath==null)
			dxfPath = (String) dp.get("Original File");
		//System.out.println("dxfPath: "+dxfPath);
		String layer = (String) dp.get("LAYER");
		System.out.println("Layer: "+layer);
		return createFeatureCollectionFromSelectionSpecial(layer,dxfPath);
	}

	/**
	 * TODO Estudiar bien los Query Chooser para que se puedan devolver
	 * todos los FeatureCollection implicitamente contenidos en el.
	 *
	 * @param selectedFile
	 * @return
	 */
	private FeatureCollection createFeatureCollectionFromSelection(String selectedFile) {
		//System.out.println("[DxfReader.createFeatureCollectionFromSelection()] Inicio.");
		GeopistaDxfConverter dxfConverter = new GeopistaDxfConverter();
		dxfConverter.convert(selectedFile);
		return (FeatureCollection) dxfConverter.getFeatureCollection(null);
	}
	
	
	private FeatureCollection createFeatureCollectionFromSelectionSpecial(String layer,
																					String selectedFile) {
		
		ArrayList solucion = new ArrayList();
		
		//System.out.println("[DxfReader.createFeatureCollectionFromSelection()] Inicio.");
		GeopistaDxfConverter dxfConverter = new GeopistaDxfConverter();
		dxfConverter.convert(selectedFile);
		

		FeatureSchema schema=null;
		FeatureSchema schemaTemp=null;
		FeatureCollection featureCollection;
		
		/* POR AQUI NO ENTRA NUNCA POR LO QUE LO ELIMINAMOS
		 for (Enumeration eLayers=dxfConverter.getLayers();eLayers.hasMoreElements();){
            String sLayer=(String)eLayers.nextElement();
            if (layer.equals(sLayer)){
            	FeatureCollection fc=dxfConverter.getFeatureCollection(sLayer);
                solucion.add(fc);                
            }
        }*/
		
		boolean featuresEncontradas=false;
		String nombreCapaSimple=layer;
		
		//Cuando en el layerable hay capas con el mismo nombre, aparece con 
		//un identificador al final que marca el indice de la capa dentro del layerable
		//Esto complica la obtencion de la informacion.	Normalmente solo puede haber dos
		//una de lineas, poligonos, y puntos y luego otra de textos que aplica a una de las 
		//otras.
		int indiceCapa=getNumeroIndiceCapa(layer);
		if (indiceCapa!=-1)
			nombreCapaSimple=getNombreCapa(layer);
		
		Iterator pointsIterator = ((FeatureCollectionDxfConverter)dxfConverter).getPointFeatureCollection().iterator();
        featureCollection=getFeatures(nombreCapaSimple,pointsIterator, (FeatureCollectionDxfConverter)dxfConverter);
        if ((featureCollection!=null) && (indiceCapa==-1)){
        	schemaTemp=addFeatures(featureCollection,solucion,(FeatureCollectionDxfConverter)dxfConverter);
        	if (schemaTemp!=null)
        		schema=schemaTemp;
        	featuresEncontradas=true;
        }

		if (!featuresEncontradas){
	        Iterator linesIterator = dxfConverter.getLineFeatureCollection().iterator();
	        featureCollection=getFeatures(nombreCapaSimple,linesIterator, (FeatureCollectionDxfConverter)dxfConverter);
	        if ((featureCollection!=null) && (indiceCapa==-1)){
	        	schemaTemp=addFeatures(featureCollection,solucion,(FeatureCollectionDxfConverter)dxfConverter);
	        	if (schemaTemp!=null)
	        		schema=schemaTemp;
	        	featuresEncontradas=true;
	        }
		}
		
		if (!featuresEncontradas){
	        Iterator polygonsIterator = dxfConverter.getPolygonFeatureCollection().iterator();
	        featureCollection=getFeatures(nombreCapaSimple,polygonsIterator, (FeatureCollectionDxfConverter)dxfConverter);
	        if ((featureCollection!=null) && (indiceCapa==-1)){
	        	schemaTemp=addFeatures(featureCollection,solucion,(FeatureCollectionDxfConverter)dxfConverter);
	        	if (schemaTemp!=null)
	        		schema=schemaTemp;
	        	featuresEncontradas=true;
	        }
		}
        
		if (!featuresEncontradas){
	        Iterator textIterator = dxfConverter.getTextFeatureCollection().iterator();
	        featureCollection=getFeatures(nombreCapaSimple,textIterator, (FeatureCollectionDxfConverter)dxfConverter);
	    	schemaTemp=addFeatures(featureCollection,solucion,(FeatureCollectionDxfConverter)dxfConverter);
	    	if (schemaTemp!=null)
	    		schema=schemaTemp;
	    	featuresEncontradas=true;
		}
		
		FeatureCollection fcRet=new FeatureDataset(schema);
		fcRet.addAll(solucion);
		
		return fcRet;
	}
	
	/**
	 * Obtiene las features de la capa seleccionada y devuelve el schema de la capa
	 * @param layer
	 * @param dataSourceQueryCollection
	 * @param featureCollectionIterator
	 * @param converter
	 * @return
	 */
	private FeatureCollection getFeatures(String layer,
            Iterator featureCollectionIterator, FeatureCollectionDxfConverter converter){

		FeatureCollection featureCollection=null;
		
		while(featureCollectionIterator.hasNext()){
			featureCollection =
					(FeatureCollection) featureCollectionIterator.next();
			String layerName = converter.getFeatureCollectionName(featureCollection);
			if (layer.equals(layerName)){
				return featureCollection;
			}
			
		}
		return null;
		
	}
	
	private FeatureSchema addFeatures(FeatureCollection featureCollection,
										ArrayList dataSourceQueryCollection,
										 FeatureCollectionDxfConverter converter){
		FeatureSchema schema=null;
		schema=featureCollection.getFeatureSchema();
		List features=featureCollection.getFeatures();
		Iterator it=features.iterator();
		while (it.hasNext()){			
			BasicFeature feature=(BasicFeature)it.next();
			dataSourceQueryCollection.add(feature);
		}		
		return schema;
	}
	
	/**
	 * Devuelve el indice de la capa si esta repetida
	 * @return
	 */
	private int getNumeroIndiceCapa(String layer){
		
		int indiceCapa=-1;
		//El grupo 0 es el nombre de la capa, el 1 el identificador de la capa.
		int INDICE_NUMERO_CAPA=2;
		Pattern capa = Pattern.compile("(.*)\\s\\((.*)\\)");
		Matcher fit=capa.matcher(layer);
		if (fit.matches()){
			indiceCapa=Integer.parseInt(fit.group(INDICE_NUMERO_CAPA),10);
		}
		return indiceCapa;
	}
	
	private String getNombreCapa(String layer){
		
		String nombreCapa=layer;
		//El grupo 0 es el nombre de la capa, el 1 el identificador de la capa.
		int INDICE_NOMBRE_CAPA=1;
		Pattern capa = Pattern.compile("(.*)\\s\\((.*)\\)");
		Matcher fit=capa.matcher(layer);
		if (fit.matches()){
			nombreCapa=fit.group(INDICE_NOMBRE_CAPA);
		}
		return nombreCapa;
	}
	
	
	/*private boolean hasFeaturesForLayer(String layer,ArrayList dataSourceQueryCollection,
            Iterator featureCollectionIterator, FeatureCollectionDxfConverter converter){

		boolean resultado=false;
		
		while(featureCollectionIterator.hasNext()){
			FeatureCollection featureCollection =
			(FeatureCollection) featureCollectionIterator.next();
			String layerName = converter.getFeatureCollectionName(featureCollection);
			if (layer.equals(layerName)){
				return tre
				schema=featureCollection.getFeatureSchema();
				List features=featureCollection.getFeatures();
				Iterator it=features.iterator();
				while (it.hasNext()){
					BasicFeature feature=(BasicFeature)it.next();
					dataSourceQueryCollection.add(feature);
				}			
			}
			
		}
		return schema;
		
	}*/
	
	public static void main(String args[]){
		//Pattern capa = Pattern.compile("//.*(//.*)");
		Pattern capa = Pattern.compile("(.*)\\s\\((.*)\\)");
		Matcher fit=capa.matcher("PG-CO (2)");
		if (fit.matches()){
			System.out.println(fit.group(2));
			System.out.println("Match");
		}
		else{
			System.out.println("No match");
		}
		
	}
	
}
