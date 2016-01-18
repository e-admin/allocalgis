/**
 * ACDynamicLayerBase.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/** Datos de una capa para el interfaz con el administrador de cartografia */
public class ACDynamicLayerBase extends ACLayerBase implements Serializable{
	
	private static final Log	logger	= LogFactory.getLog(ACDynamicLayerBase.class);


    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private Integer id;
    private String service;
    private String url;
    private List params = new ArrayList();
    private String srs;
    private String format;
    private String version;
    private String time;
    private boolean isActiva;
    private int position;
    
    /**Tabla hash con los estilos seleccionados por el usuario para cada capa*/
    private HashMap styles = new HashMap();;

    
    
    private static final String UTM_30N_ETRS89="25830";
    
	public ACDynamicLayerBase(int id,String name,String systemName,String selectQuery,String url,String time){
    	this(id,name,systemName,selectQuery,url);
    	this.time = time;
    }
    
	public ACDynamicLayerBase(int id,String name,String systemName,String selectQuery,String url){
	    super(id,name,systemName,selectQuery);
	    try{
	    	StringTokenizer st = new StringTokenizer(url,"?");
	    	if (st.hasMoreTokens()){
	    		this.url = (String)st.nextToken()+"?";
	    		String request = (String)st.nextToken();
	    		String capa = searchToken(request,"LAYERS");
	    		this.params.add(capa);
	    		srs = searchToken(request,"SRS");
	    		format = searchToken(request,"FORMAT");
	        	version = searchToken(request,"VERSION");
	        	service = searchToken(request,"SERVICE");
	    		styles.put(capa, searchToken(request,"STYLES"));
	    	}
	    	else{
	    		url = "";
				srs = "EPSG:" + UTM_30N_ETRS89;
				format = "image/png";
		    	version = "1.1.1";
		    	service = "wms";
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	

	private String searchToken(String request,String token){
		StringTokenizer st = new StringTokenizer(request,"&");
		while(st.hasMoreTokens()){
			String subcadena = ((String)st.nextToken());
			if (subcadena.toUpperCase().startsWith(token)){
	    		String[] subcadenaTroz = subcadena.split("=");
	    		if (subcadenaTroz.length==2){
	    			return subcadenaTroz[1];
	    		}
			}
		}
		return "";
	}

	public String getTime() {
		return this.time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
