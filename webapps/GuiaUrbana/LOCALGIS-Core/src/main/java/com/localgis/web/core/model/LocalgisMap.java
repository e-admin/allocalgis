/**
 * LocalgisMap.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

import com.localgis.web.core.ConstantesSQL;

public class LocalgisMap {

    private String name;

    private Integer mapid;

    private Integer mapidgeopista;

    private Double minx;

    private Double miny;

    private Double maxx;

    private Double maxy;

    private Integer mapidentidad;

    private Short mappublic;

    private String srid;
    /** SRID original, por si se cambia en algún momento (ej: 23029 -> 4236*/
    private String originalSrid;
    
    public String getOriginalSrid() {
		return originalSrid;
	}

	public void setOriginalSrid(String originalSrid) {
		this.originalSrid = originalSrid;
	}

	private Short mapdefault;
    
    public LocalgisMap() {
    }
    
    public LocalgisMap(String name, Integer mapid, Integer mapidgeopista, Double minx, Double miny, Double maxx, Double maxy, Integer mapidentidad, Short mappublic, String srid, Short mapdefault) {
        super();
        this.name = name;
        this.mapid = mapid;
        this.mapidgeopista = mapidgeopista;
        this.minx = minx;
        this.miny = miny;
        this.maxx = maxx;
        this.maxy = maxy;
        this.mapidentidad = mapidentidad;
        this.mappublic = mappublic;
        this.srid = srid;
        this.originalSrid= srid;
        this.mapdefault = mapdefault;
    }

    /*
     * El metodo no es sobrecargado para evitar problemas con ibatis a la hora de devolver un resultMap
     */
    public void setMappublicBoolean(Boolean mappublic) {
        if (mappublic != null && mappublic.booleanValue()) {
            this.mappublic = ConstantesSQL.TRUE;
        } else {
            this.mappublic = ConstantesSQL.FALSE;
        }
    }

    /*
     * El metodo no es sobrecargado para evitar problemas con ibatis a la hora de devolver un resultMap
     */
    public void setMapdefaultBoolean(Boolean mapdefault) {
        if (mapdefault != null && mapdefault.booleanValue()) {
            this.mapdefault = ConstantesSQL.TRUE;
        } else {
            this.mapdefault = ConstantesSQL.FALSE;
        }
    }

    /**
     * Devuelve el campo name
     * 
     * @return El campo name
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el valor del campo name
     * 
     * @param name
     *            El campo name a establecer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve el campo mapid
     * 
     * @return El campo mapid
     */
    public Integer getMapid() {
        return mapid;
    }

    /**
     * Establece el valor del campo mapid
     * 
     * @param mapid
     *            El campo mapid a establecer
     */
    public void setMapid(Integer mapid) {
        this.mapid = mapid;
    }

    /**
     * Devuelve el campo mapidgeopista
     * 
     * @return El campo mapidgeopista
     */
    public Integer getMapidgeopista() {
        return mapidgeopista;
    }

    /**
     * Establece el valor del campo mapidgeopista
     * 
     * @param mapidgeopista
     *            El campo mapidgeopista a establecer
     */
    public void setMapidgeopista(Integer mapidgeopista) {
        this.mapidgeopista = mapidgeopista;
    }

    /**
     * Devuelve el campo minx
     * 
     * @return El campo minx
     */
    public Double getMinx() {
        return minx;
    }

    /**
     * Establece el valor del campo minx
     * 
     * @param minx
     *            El campo minx a establecer
     */
    public void setMinx(Double minx) {
        this.minx = minx;
    }

    /**
     * Devuelve el campo miny
     * 
     * @return El campo miny
     */
    public Double getMiny() {
        return miny;
    }

    /**
     * Establece el valor del campo miny
     * 
     * @param miny
     *            El campo miny a establecer
     */
    public void setMiny(Double miny) {
        this.miny = miny;
    }

    /**
     * Devuelve el campo maxx
     * 
     * @return El campo maxx
     */
    public Double getMaxx() {
        return maxx;
    }

    /**
     * Establece el valor del campo maxx
     * 
     * @param maxx
     *            El campo maxx a establecer
     */
    public void setMaxx(Double maxx) {
        this.maxx = maxx;
    }

    /**
     * Devuelve el campo maxy
     * 
     * @return El campo maxy
     */
    public Double getMaxy() {
        return maxy;
    }

    /**
     * Establece el valor del campo maxy
     * 
     * @param maxy
     *            El campo maxy a establecer
     */
    public void setMaxy(Double maxy) {
        this.maxy = maxy;
    }

    /**
     * Devuelve el campo mapidentidad
     * 
     * @return El campo mapidentidad
     */
    public Integer getMapidentidad() {
        return mapidentidad;
    }

    /**
     * Establece el valor del campo mapidentidad
     * 
     * @param mapidentidad
     *            El campo mapidentidad a establecer
     */
    public void setMapidentidad(Integer mapidentidad) {
        this.mapidentidad = mapidentidad;
    }

    /**
     * Devuelve el campo mappublic
     * 
     * @return El campo mappublic
     */
    public Short getMappublic() {
        return mappublic;
    }

    /**
     * Establece el valor del campo mappublic
     * 
     * @param mappublic
     *            El campo mappublic a establecer
     */
    public void setMappublic(Short mappublic) {
        this.mappublic = mappublic;
    }

    /**
     * Devuelve el campo srid
     * @return El campo srid
     */
    public String getSrid() {
        return srid;
    }

    /**
     * Establece el valor del campo srid
     * @param srid El campo srid a establecer
     */
    public void setSrid(String srid) {
        this.srid = srid;
    }

    /**
     * Devuelve el campo mapdefault
     * @return El campo mapdefault
     */
    public Short getMapdefault() {
        return mapdefault;
    }

    /**
     * Establece el valor del campo mapdefault
     * @param mapdefault El campo mapdefault a establecer
     */
    public void setMapdefault(Short mapdefault) {
        this.mapdefault = mapdefault;
    }

}