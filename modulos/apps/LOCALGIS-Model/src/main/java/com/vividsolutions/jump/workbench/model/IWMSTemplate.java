/**
 * IWMSTemplate.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.model;

import java.util.HashMap;
import java.util.List;

public interface IWMSTemplate {

	public Integer getId() ;
	
	public void setId(Integer id);
	
	
	public String getService();
	
	public void setService(String service);
	
	public String getUrl();
	
	public void setUrl(String url);
	
	public List getLayers();
	
	public void setLayers(String params);
	
	
	public void setLayers(List layers);
	
	public void removeAllLayers();
	
	
	   
	   
	public String getCommaSeparatedLayerNamesList();
	   
	
	public String getSrs();
	
	public void setSrs(String srs);
	
	public String getFormat();
	
	public void setFormat(String format);
	
	public String getVersion();
	
	public void setVersion(String version);
	
	public boolean isActiva();
	
	public void setActiva(boolean isActiva);
	
	public boolean isVisible();
	
	public void setVisible(boolean isVisible);
	
	public HashMap getStyles();
	
	public void setStyles(HashMap styles);
	
	public void setStyles(String styles);
	
	
	    
    public String getCommaSeparatedStylesList();
    	
	
	public String getDescripcion();
	
	public void setDescripcion(String descripcion);
	
	
	public int getAlpha();

	public void setAlpha(int alpha);
	
	public void addLayer(String layerName);
    	
}