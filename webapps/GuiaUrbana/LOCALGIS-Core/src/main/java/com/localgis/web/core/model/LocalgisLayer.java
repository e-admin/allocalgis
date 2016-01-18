/**
 * LocalgisLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;

public class LocalgisLayer {

    private Integer layerid;

    private Integer layeridgeopista;

    private String layername;

    private String layerquery;

    private String geometrytype;   

    private Integer versionable;
    
    private String time;
    private String columnTime;

	private Integer mapId;

    public LocalgisLayer() {
    }

    public LocalgisLayer(Integer layerid, Integer layeridgeopista, String layername, String layerquery, String geometrytype) {
        super();
        this.layerid = layerid;
        this.layeridgeopista = layeridgeopista;
        this.layername = layername;
        this.layerquery = layerquery;
        this.geometrytype = geometrytype;
    }

    /**
     * Devuelve el campo layerid
     * 
     * @return El campo layerid
     */
    public Integer getLayerid() {
        return layerid;
    }

    /**
     * Establece el valor del campo layerid
     * 
     * @param layerid
     *            El campo layerid a establecer
     */
    public void setLayerid(Integer layerid) {
        this.layerid = layerid;
    }

    /**
     * Devuelve el campo layeridgeopista
     * 
     * @return El campo layeridgeopista
     */
    public Integer getLayeridgeopista() {
        return layeridgeopista;
    }

    /**
     * Establece el valor del campo layeridgeopista
     * 
     * @param layeridgeopista
     *            El campo layeridgeopista a establecer
     */
    public void setLayeridgeopista(Integer layeridgeopista) {
        this.layeridgeopista = layeridgeopista;
    }

    /**
     * Devuelve el campo layername
     * 
     * @return El campo layername
     */
    public String getLayername() {
        return layername;
    }

    /**
     * Establece el valor del campo layername
     * 
     * @param layername
     *            El campo layername a establecer
     */
    public void setLayername(String layername) {
        this.layername = layername;
    }

    /**
     * Devuelve el campo layerquery
     * 
     * @return El campo layerquery
     */
    public String getLayerquery() {
        return layerquery;
    }

    /**
     * Establece el valor del campo layerquery
     * 
     * @param layerquery
     *            El campo layerquery a establecer
     */
    public void setLayerquery(String layerquery) {
        this.layerquery = layerquery;
    }

    /**
     * Devuelve el campo geometrytype
     * @return El campo geometrytype
     */
    public String getGeometrytype() {
        return geometrytype;
    }

    /**
     * Establece el valor del campo geometrytype
     * @param geometrytype El campo geometrytype a establecer
     */
    public void setGeometrytype(String geometrytype) {
        this.geometrytype = geometrytype;
    }

    public Integer getVersionable() {
        return versionable;
    }

    public void setVersionable(Integer versionable) {
        this.versionable = versionable;
    }
    
    public boolean isVersionable() {
    	
    	/*String publicarVersionables=null;
    	try {
			publicarVersionables=Configuration.getPropertyString("mapserver.publicarcapasversionables");
		} catch (LocalgisConfigurationException e) {
		}
    	if ((publicarVersionables!=null) && (publicarVersionables.equals("0")))
    			return false;*/
	    	
		boolean isVersionable = false;
		if (versionable.intValue() == 1) {
		    isVersionable = true;
		}
	        return isVersionable;
    }
    
    public boolean isVersionable2() {
    	
    		    	
		boolean isVersionable = false;
		if (versionable.intValue() == 1) {
		    isVersionable = true;
		}
	        return isVersionable;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }

    public String getColumnTime() {
        return columnTime;
    }
    
    public void setColumnTime(String columnTime) {
        this.columnTime = columnTime;
    }

	public Integer getMapId() {
		return this.mapId;
	}
    
	public void setMapId(int mapId){
		this.mapId=mapId;
	}

}