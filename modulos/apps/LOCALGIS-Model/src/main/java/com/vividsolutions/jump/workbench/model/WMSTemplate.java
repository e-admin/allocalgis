/**
 * WMSTemplate.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class WMSTemplate implements Serializable,IWMSTemplate{
    private Integer id;
    private String service;
    private String url;
    private List layers;
    private String srs;
    private String format;
    private String version;
    private boolean isActiva;
    private boolean isVisible;
    private HashMap styles;
    private String descripcion;
    private int alpha = 255;
    
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List getLayers() {
		return Collections.unmodifiableList(layers);
	}
	public void setLayers(String params) {
		this.layers = buildLayerNamesList(params);
		
	}
	
	public void setLayers(List layers){
		this.layers=layers;
	}
	
	public void removeAllLayers() {
		layers.clear();
	}
	
	
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
	   
	   
	   public String getCommaSeparatedLayerNamesList(){
	        String list = "";

	        if(layers!=null){
	        for (Iterator it = layers.iterator(); it.hasNext();){
	            if(list.equals(""))
	               list = (String)it.next();
	            else
	                list = list + "," + (String)it.next();
	        }//fin for
	        }//fin if

	        return list;
	    }
	   
	
	public String getSrs() {
		return srs;
	}
	public void setSrs(String srs) {
		this.srs = srs;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public boolean isActiva() {
		return isActiva;
	}
	public void setActiva(boolean isActiva) {
		this.isActiva = isActiva;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	public HashMap getStyles() {
		return styles;
	}
	
	public void setStyles(HashMap styles){
		this.styles=styles;
	}
	
	public void setStyles(String styles) {
		this.styles = buildStylesMap(styles);
	}
	
	
	
	
    private HashMap buildStylesMap(String styles){
    	HashMap stylesMap=new HashMap();
    	if(styles!=null&&layers!=null){
    	if(styles.indexOf(",") < 0){//si sólo hay una capa y un estilo
    		String layerName=(String) this.layers.get(0);
    		stylesMap.put(layerName,styles);
        }
    	else{
    		 String[] theStyles = styles.split(",");
             for(int i=0; i<theStyles.length; i++){
            	 String layerName=(String) layers.get(i);
            	 System.out.println(theStyles[i]);
                 stylesMap.put(layerName,theStyles[i]);
             }	
    	}
    		
    	}//fin if
    	return stylesMap;	
    }
    
    public String getCommaSeparatedStylesList(){
        String list = "";
       if(styles!=null&&layers!=null){
    	 
        for (Iterator it = layers.iterator(); it.hasNext();){
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
    
    
    
   
    
	
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	public int getAlpha() {
		return alpha;
	}

	/**
	 * @param alpha
	 *            0-255 (255 is opaque)
	 */
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
	
	
	public void addLayer(String layerName) {
		if(layers==null)
			layers=new LinkedList();
		layers.add(layerName);
	}
	
	public String toString(){
		return descripcion;
	}//fin toString
    
    
    
	
	
	
	
	
	
	
		
		
		
		
}//fin clase WMSTemplate
