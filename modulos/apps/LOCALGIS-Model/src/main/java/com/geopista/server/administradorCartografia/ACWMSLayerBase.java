/**
 * ACWMSLayerBase.java
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
import java.util.Iterator;
import java.util.List;


public class ACWMSLayerBase implements Serializable,IACWMSLayer{

	
    private Integer id;
    private String service;
    private String url;
    
    private String name;

    private List params;
    private String srs;
    private String format;
    private String version;
    private boolean isActiva;
    private boolean isVisible;
    private int position;
    /**Tabla hash con los estilos seleccionados por el usuario para cada capa*/
    private HashMap styles;
    
    private boolean isTemplate;
    private String descripcion;
    //atributos que sólo se utilizarán para las capas externa
    private String layerId;
    private String table;
    private String idField;
    private String geometryField;

    public ACWMSLayerBase(int id, String service, String url, String params, String srs, String format, 
    		String version, boolean activa, boolean visible, int position, String styles, String wmsName) {
        this.id = new Integer(id);
        this.service = service;
        this.url = url;
        this.params = buildLayerNamesList(params);
        this.srs = srs;
        this.format = format;
        this.version = version;
        this.isActiva = activa;
        this.isVisible = visible;
        this.position = position;
        this.styles=buildStylesMap(styles);
        this.name=wmsName;
    }

      
   
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isActiva() {
        return isActiva;
    }

    public void setActiva(boolean activa) {
        isActiva = activa;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSrs() {
        return srs;
    }

    public void setSrs(String srs) {
        this.srs = srs;
    }

    public List getParams() {
        return params;
    }

    public void setParams(List params) {
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
   

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public HashMap getStyles() {
		return styles;
	}

	public void setStyles(HashMap styles) {
		this.styles = styles;
	}
	/*
    public boolean isTemplate() {
		return isTemplate;
		
	}

	public void setTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}*/
	
	

	/*public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
	public String getTable() {
		return table;
	}



	public void setTable(String table) {
		this.table = table;
	}
	


	public String getIdField() {
		return idField;
	}



	public void setIdField(String idField) {
		this.idField = idField;
	}



	public String getGeometryField() {
		return geometryField;
	}



	public void setGeometryField(String geometryField) {
		this.geometryField = geometryField;
	}*/
	
	
	
	private List buildLayerNamesList(String commaSeparatedLayerNamesList){
        ArrayList layerNames = new ArrayList();
        if(commaSeparatedLayerNamesList.indexOf(",") < 0){
            layerNames.add(commaSeparatedLayerNamesList);
        }
        else{
            String[] names = commaSeparatedLayerNamesList.split(",");
            for(int i=0; i<names.length; i++){
                layerNames.add(names[i]);
            }
        }
        return layerNames;
    }
	
	private HashMap buildStylesMap(String styles){
    	HashMap stylesMap=new HashMap();
    	if(styles!=null){
    	if(styles.indexOf(",") < 0){//si sólo hay una capa y un estilo
    		String layerName=(String) this.params.get(0);
    		stylesMap.put(layerName,styles);
        }
    	else{
    		 String[] theStyles = styles.split(",");
             for(int i=0; i<theStyles.length; i++){
            	 String layerName=(String) params.get(i);
            	 System.out.println(theStyles[i]);
                 stylesMap.put(layerName,theStyles[i]);
             }	
    	}
    		
    	}//fin if
    	return stylesMap;	
    }


	
    public String getCommaSeparatedLayerNamesList(){
        String list = "";

        for (Iterator it = params.iterator(); it.hasNext();){
            if(list.equals(""))
               list = (String)it.next();
            else
                list = list + "," + (String)it.next();
        }

        return list;
    }
    
    public String getCommaSeparatedStylesList(){
        String list = "";
       if(styles!=null){
    	 
        for (Iterator it = params.iterator(); it.hasNext();){
        	String layerName=(String)it.next();
        	 String style = (String) styles.get(layerName);
        	 
        	 if(style==null){
        		 list+="default";
        		 if(it.hasNext())
        			 list+=",";
        	 }
        	 
        	 else{
        		 if(it.hasNext())
        			 list+=style+",";
        		 else
        			 list+=style;
        	 }
        		 
            
        }//fin del for
       
       }
        return list;
    }
    
}
