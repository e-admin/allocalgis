/**
 * LocalgisAttribute.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

public class LocalgisAttribute {

    private Integer attributeid;

    private String attributename;

    private Integer attributeidalias;

    private Integer attributeidgeopista;

    private Integer layerid;

	private Integer mapid;

    public LocalgisAttribute() {
    }
    
    public LocalgisAttribute(Integer attributeid, String attributename, Integer attributeidalias, Integer attributeidgeopista, Integer layerid) {
        super();
        this.attributeid = attributeid;
        this.attributename = attributename;
        this.attributeidalias = attributeidalias;
        this.attributeidgeopista = attributeidgeopista;
        this.layerid = layerid;
    }

    /**
     * Devuelve el campo attributeid
     * 
     * @return El campo attributeid
     */
    public Integer getAttributeid() {
        return attributeid;
    }

    /**
     * Establece el valor del campo attributeid
     * 
     * @param attributeid
     *            El campo attributeid a establecer
     */
    public void setAttributeid(Integer attributeid) {
        this.attributeid = attributeid;
    }

    /**
     * Devuelve el campo attributename
     * 
     * @return El campo attributename
     */
    public String getAttributename() {
        return attributename;
    }

    /**
     * Establece el valor del campo attributename
     * 
     * @param attributename
     *            El campo attributename a establecer
     */
    public void setAttributename(String attributename) {
        this.attributename = attributename;
    }

    /**
     * Devuelve el campo attributeidgeopista
     * 
     * @return El campo attributeidgeopista
     */
    public Integer getAttributeidgeopista() {
        return attributeidgeopista;
    }

    /**
     * Establece el valor del campo attributeidgeopista
     * 
     * @param attributeidgeopista
     *            El campo attributeidgeopista a establecer
     */
    public void setAttributeidgeopista(Integer attributeidgeopista) {
        this.attributeidgeopista = attributeidgeopista;
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
     * Devuelve el campo attributeidalias
     * @return El campo attributeidalias
     */
    public Integer getAttributeidalias() {
        return attributeidalias;
    }

    /**
     * Establece el valor del campo attributeidalias
     * @param attributeidalias El campo attributeidalias a establecer
     */
    public void setAttributeidalias(Integer attributeidalias) {
        this.attributeidalias = attributeidalias;
    }

	public void setMapid(Integer mapid) {
		this.mapid=mapid;		
	}

	public Integer getMapid() {
		return this.mapid;		
	}
    
}