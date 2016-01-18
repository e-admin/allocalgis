/**
 * LayerUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.util;

import org.apache.commons.lang.StringUtils;

import com.localgis.web.core.model.LocalgisLayer;


public class LayerUtils {

	
	/**
     * El nombre de la tabla lo saca de la tabla indicada tras el TRANSFORM
     * El municipio de IN (xxxx)
     * @param layer
     * @return
     */
    public static String[] getTableNameFromLayer(LocalgisLayer layer) {
    	String[] transform = {"transform(", "TRANSFORM("};
    	String[] in = {" in ", " IN "};

    	int transPos = -1;
    	int inPos = -1;
    	int leftParPos = -1;
    	int rightParPos = -1;
    	
    	String[] datos = new String[5];
    	
    	String sql = layer.getLayerquery();
    	
    	//Nombre de tabla
    	transPos = StringUtils.lastIndexOfAny(sql, transform);
    	String subStrTransform = sql.substring(transPos + transform[0].length());
    	
    	datos[0] = subStrTransform.substring(0, subStrTransform.indexOf("."));
    	//datos[0] = datos[0].toLowerCase();
    	//datos[0] = StringUtils.remove(datos[0], "\"");
    	StringUtils.trim(datos[0]);
    	if (datos[0].startsWith("\"GEOMETRY")){
			/*
			 * Por aqui no debería entrar nunca está para solucionar una
			 * indicencia cuando en el campo layerquery de la tabla
			 * localgisguiaurbana.layer no tenemos
			 * .....transform("tabla"."GEOMETRY" sino ...transform("GEOMETRY*...
			 * Lo cojo del ppio SELECT "nombreTabla".oid...
			 */	
    		int finTablaPos=-1;
    		String nombreTabla="";
    		finTablaPos=sql.indexOf("\".");
    		//8=SELECT+ESPACIO
    		nombreTabla=sql.substring(8,finTablaPos);
    		datos[0]=nombreTabla.trim();
    	}
    	
    	
    	
    	//Municipio
    	inPos = StringUtils.lastIndexOfAny(sql, in);
    	String subStrIn = sql.substring(inPos + 2);
    	leftParPos = subStrIn.indexOf("(");
    	rightParPos = subStrIn.indexOf(")");
    	
    	if(leftParPos != -1 && rightParPos != -1) {
    		datos[1] = subStrIn.substring(leftParPos + 1, rightParPos);
    		StringUtils.trim(datos[1]);
    	} else {
    		datos[1] = null;
    	}
    	
    	return datos;
    }  
}
