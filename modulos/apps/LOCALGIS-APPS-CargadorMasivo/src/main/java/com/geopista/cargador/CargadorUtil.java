/**
 * CargadorUtil.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.cargador;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CargadorUtil {
	
	private static final Log	logger	= LogFactory.getLog(CargadorUtil.class);

	//carpeta contenedora de los ficheros nombrada con el código ine del municipio correspondiente
	public static String folderMunicipio = ""; 
	// contains path of folder Resultado Base
	public static String folderResultadoBase = "";
	public static String homeCartografia = "CartografiaBase" + File.separator	+ "Municipios";

	
	
	/**
	* Filtra texto html convirtiéndolo en texto plano
	* 
	* @param String
	*            texto, texto para filtrar
	* @return String
	* 			texto, texto una vez filtrado de tags html
	*/       
	public static String filterText(String texto){
	  int indexIni=0;
	  int indexFin=0;
	  while(indexIni!=-1 && indexFin!=-1){
		  indexIni=texto.indexOf("<");
		  indexFin=texto.indexOf(">");
		  if(indexIni!=-1 && indexFin!=-1){
			  if(texto.substring(indexIni+1,indexFin).equals("<br>"))
				  texto=texto.substring(0,indexIni)+"\n"+texto.substring(indexFin+1,texto.length());
			  else
				  texto=texto.substring(0,indexIni)+texto.substring(indexFin+1,texto.length());
		  }
	  }
	  return texto;
	}

}
