/**
 * GpxGenerator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.satec.gpx;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.log4j.Logger;

import com.satec.gpx.gpx11.GpxType;
import com.satec.gpx.gpx11.RteType;
import com.satec.gpx.gpx11.WptType;
import com.satec.gpx.utils.Constants;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.text.StrTokenizer;

public class GpxGenerator {
	
	protected Logger log = Logger.getLogger(GpxGenerator.class);
	
	//Lista de rutas
	private ArrayList<RteType> listaRutas = new ArrayList<RteType>();

	/**
	 * Genera un objeto GpxType a partir de los datos obtenidos de una capa GIS en una lista.
	 * El contenido de la lista es similar a:<br>
	 * LINESTRING (199 328, 293 423, 432 427, 510 470, 561 481, 573.6996389891697 431.1783393501805, 587 379, 609 350)
	 * @param data Lista con las rutas
	 * @return Objeto GpxType
	 */
	public GpxType generateGpx(ArrayList<String> data) {
		String actualData = "";
		
		//Procesar todos los elementos
		for(int i = 0; i < data.size(); i++) {
			actualData = StringUtils.trim(data.get(i));
			procesarDatos(actualData, i);
		}
		
		//Generar objeto GPX
		GpxType gpx = new GpxType();
		gpx.setCreator(getClass().getName());
		gpx.getRte().addAll(listaRutas);
		
		log.info("Generado objeto GPX");
		
		return gpx;
	}
	
	/**
	 * Genera un XML, encapsulado en un ByteArrayOutputStream, a partir de los datos obtenidos de una capa GIS.
	 * Internamente este método llama al método {@link generateGpx}
	 * @param data Lista con las rutas
	 * @return ByteArrayOutputStream conteniendo el XML generado
	 */
	public ByteArrayOutputStream generateGpxOutputStream(ArrayList<String> data) {
		GpxType gpx = generateGpx(data);
		Gpx11Writer writer = new Gpx11Writer();
		return writer.convertGpx(gpx);
	}
	
	/**
	 * Genera un archivo XML a partir de los datos obtenidos de una capa GIS.
	 * @param data Lista con las rutas
	 * @param filename Nombre del archivo
	 * @return Archivo con el XML generado
	 */
	public boolean generateGpxFile(ArrayList<String> data, String filename) {
		GpxType gpx = generateGpx(data);
		Gpx11Writer writer = new Gpx11Writer();
		return writer.writeGpxFile(gpx, filename);
	}
	
	protected void procesarDatos(String data, int routeIndex) {
		log.debug("Procesando " + data + "...");
		
		//Obtener el tipo de elemento
		boolean isMultiLineString = StringUtils.containsIgnoreCase(data, Constants.MULTILINE_ROUTE_TOKEN);
		boolean isLineString = StringUtils.contains(data, Constants.LINE_ROUTE_TOKEN);
		
		//Si ambos son true, es que tenemos un MULTILINE
		if(isMultiLineString && isLineString) {
			isLineString = false;
		}
		
		//Si es una ruta "multiline"
		if(isMultiLineString) {
			String linea = preprocesarMultiLineString(data, routeIndex);
			procesarLineString(linea, routeIndex);
		}
		
		//Si es una ruta "simple"
		if(isLineString) {
			procesarLineString(data, routeIndex);
		}
	}
	
	private String preprocesarMultiLineString(String lineString, int index) {
		String resultado = "";
		//Vamos a juntar todos los "cachos" del multiline dejando solo las coordenadas
		lineString = StringUtils.remove(lineString, Constants.MULTILINE_ROUTE_TOKEN);
		lineString = StringUtils.remove(lineString, "(");
		lineString = StringUtils.remove(lineString, ")");
		lineString = StringUtils.trim(lineString);
		
		//... y regenerandolo como LINESTRING
		resultado = Constants.LINE_ROUTE_TOKEN + "(" + lineString + ")";
		
		return resultado;
	}
	
	private void procesarLineString(String lineString, int index) {
		//Crear objeto "ruta" y un waypoint para ir rellenando la lista de puntos
		WptType wpt = null;
		RteType rte = new RteType();
		rte.setName("RUTA_" + index);
		rte.setNumber(new BigInteger("" + index));
		
		//Eliminar "LINESTRING", "(" y ")"
		lineString = StringUtils.remove(lineString, Constants.LINE_ROUTE_TOKEN);
		lineString = StringUtils.remove(lineString, "(");
		lineString = StringUtils.remove(lineString, ")");
		lineString = StringUtils.trim(lineString);
		
		//Obtener array de puntos
		StrTokenizer routeTknz = new StrTokenizer(lineString, ",");
		String[] puntos = routeTknz.getTokenArray();
		
		//Obtener waypoints
		StrTokenizer wpTknr = null;
		int intermedio = 1;
		int puntoFinal = puntos.length - 1;
		
		for(int i = 0; i < puntos.length; i++) {
			wpTknr = new StrTokenizer(puntos[i]);
			String[] coord = wpTknr.getTokenArray();
			
			//setear waypoint
			wpt = new WptType();
			wpt.setLon(new BigDecimal(coord[0]));
			wpt.setLat(new BigDecimal(coord[1]));
			
			//Para el nombre usamos "inicio" para el primero, "final" para el último y "punto n" para los demás
			if(i == 0) {
				wpt.setName("Inicio");
			} else
			if(i == puntoFinal) {
				wpt.setName("Final");
			} else {
				wpt.setName("Intermedio " + intermedio);
				intermedio++;
			}
			
			//Guardar en lista de puntos de la ruta
			rte.getRtept().add(wpt);
		}
		
		//Guardar ruta en la lista de rutas
		listaRutas.add(rte);
	}
}
