/**
 * FilesUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.global;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;



/**
 * Clase de gestión de ficheros de licencias
 * @author irodriguez
 *
 */
public class FilesUtils{

	protected static Logger logger = Logger.getLogger(FilesUtils.class);

	/**
	 * Devuelve un string con el comienzo de un fichero de eiel
	 * @param eielSVG
	 * @param encabezado 
	 * @return
	 */
	public String createSkeleton(String encabezado, String grupo, String path, String skeleton) {
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		str += encabezado + "\n";
		str += grupo + "\n";			
		str += emptyFeature(path) + "\n";
		str += "<metadata>\n<![CDATA[\n";
		str += skeleton;
		str += "]]>\n</metadata>\n";
		if(path.toLowerCase().startsWith("<path")){
			str += "</path>\n";
		}else if(path.toLowerCase().startsWith("<polyline")){
			str += "</polyline>\n";
		}else if(path.toLowerCase().startsWith("<line")){
			str += "</line>\n";
		}else if(path.toLowerCase().startsWith("<point")){
			str += "</point>\n";
		}else if(path.toLowerCase().startsWith("<circle")){
			str += "</circle>\n";
		}
		str += "</g></svg>";
		return str;
	}
		
	/**
	 * Imprime un doc sin la cabecera inicial de encoding
	 * @param doc
	 * @return
	 */
	public String printDoc(Document doc) {
		// Creamos el serializador con el formato deseado  
		XMLOutputter xmlOut= new XMLOutputter();  
		// Serializamos el document parseado  
		String docStr = xmlOut.outputString(doc);
		docStr = docStr.substring(40); //para quitar el <?xml version="1.0" encoding="UTF-8"?>
		return docStr;
	}

	/**
	 * Vacía una featura para corresponder con el formato: <path v1="-1" v2="-1" d="" >
	 * @param src
	 * @return
	 */
	public String emptyFeature(String src){
		String firstPart = "";
		String secondPart = src;
		String result = "";
		boolean contLoop = true;
		int firstQuote = 0;
		int lastQuote = 0;
		String quote = "\"";
		while(contLoop){
			firstQuote = secondPart.indexOf(quote);
			if(firstQuote<0){
				result += ">";
				break;
			}
			lastQuote = secondPart.indexOf(quote, firstQuote+1);
			firstPart = secondPart.substring(0, firstQuote+1);
			secondPart = secondPart.substring(lastQuote+1, secondPart.length());
			if(firstPart.contains("d=")){
				result += firstPart + "M 1 2 L 3 4 L 5 6 Z" + quote;
			} 
			else if(firstPart.contains("points=")){
				result += firstPart + "1.0,2.0 3.0,4.0" + quote;
			}
			else {
				result += firstPart + "-1" + quote;
			}
		}
		return result;
	}
	
}
