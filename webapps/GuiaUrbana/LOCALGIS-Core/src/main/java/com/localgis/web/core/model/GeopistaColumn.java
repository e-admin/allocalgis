/**
 * GeopistaColumn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

public class GeopistaColumn {

    private Integer id;

    private String name;

    private Integer idDomain;

    private Integer idTable;

    private Short length;

    private Short precision;

    private Short scale;

    private Short type;

    private Integer idAttributeGeopista;
    
    private Short geometryType;
    
    private String alias;

    private Integer idAlias;
    
    private String tablename;

    /**
     * Devuelve el campo id
     * @return El campo id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el valor del campo id
     * @param id El campo id a establecer
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve el campo name
     * @return El campo name
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el valor del campo name
     * @param name El campo name a establecer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve el campo idDomain
     * @return El campo idDomain
     */
    public Integer getIdDomain() {
        return idDomain;
    }

    /**
     * Establece el valor del campo idDomain
     * @param idDomain El campo idDomain a establecer
     */
    public void setIdDomain(Integer idDomain) {
        this.idDomain = idDomain;
    }

    /**
     * Devuelve el campo idTable
     * @return El campo idTable
     */
    public Integer getIdTable() {
        return idTable;
    }

    /**
     * Establece el valor del campo idTable
     * @param idTable El campo idTable a establecer
     */
    public void setIdTable(Integer idTable) {
        this.idTable = idTable;
    }

    /**
     * Devuelve el campo length
     * @return El campo length
     */
    public Short getLength() {
        return length;
    }

    /**
     * Establece el valor del campo length
     * @param length El campo length a establecer
     */
    public void setLength(Short length) {
        this.length = length;
    }

    /**
     * Devuelve el campo precision
     * @return El campo precision
     */
    public Short getPrecision() {
        return precision;
    }

    /**
     * Establece el valor del campo precision
     * @param precision El campo precision a establecer
     */
    public void setPrecision(Short precision) {
        this.precision = precision;
    }

    /**
     * Devuelve el campo scale
     * @return El campo scale
     */
    public Short getScale() {
        return scale;
    }

    /**
     * Establece el valor del campo scale
     * @param scale El campo scale a establecer
     */
    public void setScale(Short scale) {
        this.scale = scale;
    }

    /**
     * Devuelve el campo type
     * @return El campo type
     */
    public Short getType() {
        return type;
    }

    /**
     * Establece el valor del campo type
     * @param type El campo type a establecer
     */
    public void setType(Short type) {
        this.type = type;
    }

    /**
     * Devuelve el campo idAttributeGeopista
     * @return El campo idAttributeGeopista
     */
    public Integer getIdAttributeGeopista() {
        return idAttributeGeopista;
    }

    /**
     * Establece el valor del campo idAttributeGeopista
     * @param idAttributeGeopista El campo idAttributeGeopista a establecer
     */
    public void setIdAttributeGeopista(Integer idAttributeGeopista) {
        this.idAttributeGeopista = idAttributeGeopista;
    }

    /**
     * Devuelve el campo geometryType
     * @return El campo geometryType
     */
    public Short getGeometryType() {
        return geometryType;
    }

    /**
     * Establece el valor del campo geometryType
     * @param geometryType El campo geometryType a establecer
     */
    public void setGeometryType(Short geometryType) {
        this.geometryType = geometryType;
    }

    /**
     * Devuelve el campo alias
     * @return El campo alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Establece el valor del campo alias
     * @param alias El campo alias a establecer
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Devuelve el campo tablename
     * @return El campo tablename
     */
    public String getTablename() {
        return tablename;
    }

    /**
     * Establece el valor del campo tablename
     * @param tablename El campo tablename a establecer
     */
    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    /**
     * Devuelve el campo idAlias
     * @return El campo idAlias
     */
    public Integer getIdAlias() {
        return idAlias;
    }

    /**
     * Establece el valor del campo idAlias
     * @param idAlias El campo idAlias a establecer
     */
    public void setIdAlias(Integer idAlias) {
        this.idAlias = idAlias;
    }

}