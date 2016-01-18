/**
 * LocalgisLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administrador.web;

import java.util.Map;

public class LocalgisLayer {

    private int layerid;
    private String layername;
    private String layerquery;
    private int geometrytype;
    private String styleName;
    private String xml;
    private String srid;
    private int idEntidad;
    private String service;
    private String time;
    private String columnTime;
    private String mapaAtributos;
    

    public String getMapaAtributos() {
		return mapaAtributos;
	}

	public void setMapaAtributos(String mapaAtributos) {
		this.mapaAtributos = mapaAtributos;
	}

	public LocalgisLayer() {
    }

	public int getLayerid() {
		return layerid;
	}

	public void setLayerid(int layerid) {
		this.layerid = layerid;
	}

	public String getLayername() {
		return layername;
	}

	public void setLayername(String layername) {
		this.layername = layername;
	}

	public String getLayerquery() {
		return layerquery;
	}

	public void setLayerquery(String layerquery) {
		this.layerquery = layerquery;
	}

	public int getGeometrytype() {
		return geometrytype;
	}

	public void setGeometrytype(int geometrytype) {
		this.geometrytype = geometrytype;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getSrid() {
		return srid;
	}

	public void setSrid(String srid) {
		this.srid = srid;
	}

	public int getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(int idEntidad) {
		this.idEntidad = idEntidad;
	}
	
    public String getService() {
        return service;
    }
    
    public void setService(String service) {
        this.service = service;
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
}