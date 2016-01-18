/**
 * GeopistaMunicipio.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

import java.math.BigDecimal;

public class GeopistaMunicipio implements Comparable {

    private Integer id;

    private String idProvincia;

    private String idCatastro;

    private String idMhacienda;

    private String idIne;

    private String nombreoficial;

    private String nombreoficialcorto;

    private String nombrecooficial;

    private BigDecimal area;

    private BigDecimal length;

    private String srid;
    
    
  //Creamos este metodo para poder ordenar una lista de objetos
    public int compareTo(Object o) {
    	GeopistaMunicipio otroMunicipio = (GeopistaMunicipio) o;    
    	return nombreoficial.compareTo(otroMunicipio.getNombreoficial());
    	}
    
    
    /**
     * Devuelve el campo id
     * 
     * @return El campo id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el valor del campo id
     * 
     * @param id
     *            El campo id a establecer
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve el campo idProvincia
     * 
     * @return El campo idProvincia
     */
    public String getIdProvincia() {
        return idProvincia;
    }

    /**
     * Establece el valor del campo idProvincia
     * 
     * @param idProvincia
     *            El campo idProvincia a establecer
     */
    public void setIdProvincia(String idProvincia) {
        this.idProvincia = idProvincia;
    }

    /**
     * Devuelve el campo idCatastro
     * 
     * @return El campo idCatastro
     */
    public String getIdCatastro() {
        return idCatastro;
    }

    /**
     * Establece el valor del campo idCatastro
     * 
     * @param idCatastro
     *            El campo idCatastro a establecer
     */
    public void setIdCatastro(String idCatastro) {
        this.idCatastro = idCatastro;
    }

    /**
     * Devuelve el campo idMhacienda
     * 
     * @return El campo idMhacienda
     */
    public String getIdMhacienda() {
        return idMhacienda;
    }

    /**
     * Establece el valor del campo idMhacienda
     * 
     * @param idMhacienda
     *            El campo idMhacienda a establecer
     */
    public void setIdMhacienda(String idMhacienda) {
        this.idMhacienda = idMhacienda;
    }

    /**
     * Devuelve el campo idIne
     * 
     * @return El campo idIne
     */
    public String getIdIne() {
        return idIne;
    }

    /**
     * Establece el valor del campo idIne
     * 
     * @param idIne
     *            El campo idIne a establecer
     */
    public void setIdIne(String idIne) {
        this.idIne = idIne;
    }

    /**
     * Devuelve el campo nombreoficial
     * 
     * @return El campo nombreoficial
     */
    public String getNombreoficial() {
        return nombreoficial;
    }

    /**
     * Establece el valor del campo nombreoficial
     * 
     * @param nombreoficial
     *            El campo nombreoficial a establecer
     */
    public void setNombreoficial(String nombreoficial) {
        this.nombreoficial = nombreoficial;
    }

    /**
     * Devuelve el campo nombreoficialcorto
     * 
     * @return El campo nombreoficialcorto
     */
    public String getNombreoficialcorto() {
        return nombreoficialcorto;
    }

    /**
     * Establece el valor del campo nombreoficialcorto
     * 
     * @param nombreoficialcorto
     *            El campo nombreoficialcorto a establecer
     */
    public void setNombreoficialcorto(String nombreoficialcorto) {
        this.nombreoficialcorto = nombreoficialcorto;
    }

    /**
     * Devuelve el campo nombrecooficial
     * 
     * @return El campo nombrecooficial
     */
    public String getNombrecooficial() {
        return nombrecooficial;
    }

    /**
     * Establece el valor del campo nombrecooficial
     * 
     * @param nombrecooficial
     *            El campo nombrecooficial a establecer
     */
    public void setNombrecooficial(String nombrecooficial) {
        this.nombrecooficial = nombrecooficial;
    }

    /**
     * Devuelve el campo area
     * 
     * @return El campo area
     */
    public BigDecimal getArea() {
        return area;
    }

    /**
     * Establece el valor del campo area
     * 
     * @param area
     *            El campo area a establecer
     */
    public void setArea(BigDecimal area) {
        this.area = area;
    }

    /**
     * Devuelve el campo length
     * 
     * @return El campo length
     */
    public BigDecimal getLength() {
        return length;
    }

    /**
     * Establece el valor del campo length
     * 
     * @param length
     *            El campo length a establecer
     */
    public void setLength(BigDecimal length) {
        this.length = length;
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

}