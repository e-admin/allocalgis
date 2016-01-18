/**
 * GeopistaNucleo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

import java.math.BigDecimal;
 

public class GeopistaNucleo implements Comparable   {

    private Integer id;
    private String idProvincia;
    private String idMunicipio;    
    private String nombreoficial;
    
    private String codEntidad;    
    private String idPoblamiento;
    private String geometria;
    
  
    //Creamos este metodo para poder ordenar una lista de objetos
    public int compareTo(Object o) {
    	GeopistaNucleo otroNucleo = (GeopistaNucleo) o;    
    	return nombreoficial.compareTo(otroNucleo.getNombreoficial());
    	}


     
	
	public String getGeometria() {
		return geometria;
	}




	public void setGeometria(String geometria) {
		this.geometria = geometria;
	}




	public Integer getId() {
		return id;
	}
	public String getCodEntidad() {
		return codEntidad;
	}
	public void setCodEntidad(String codEntidad) {
		this.codEntidad = codEntidad;
	}
	public String getIdPoblamiento() {
		return idPoblamiento;
	}
	public void setIdPoblamiento(String idPoblamiento) {
		this.idPoblamiento = idPoblamiento;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIdProvincia() {
		return idProvincia;
	}
	public void setIdProvincia(String idProvincia) {
		this.idProvincia = idProvincia;
	}
	public String getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(String idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public String getNombreoficial() {
		return nombreoficial;
	}
	public void setNombreoficial(String nombreoficial) {
		this.nombreoficial = nombreoficial;
	}

  
}