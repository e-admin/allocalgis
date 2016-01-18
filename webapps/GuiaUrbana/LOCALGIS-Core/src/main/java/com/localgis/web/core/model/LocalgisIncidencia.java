/**
 * LocalgisIncidencia.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

import com.vividsolutions.jts.geom.Geometry;

public class LocalgisIncidencia {
	
	private Integer srid;
	
	public Integer getSrid() {
		return srid;
	}

	public void setSrid(Integer srid) {
		this.srid = srid;
	}

	private Integer incidenciaId;

	private Integer id_municipio;
	
    private Integer mapid;

    private String identificador;

    private String valor;

    public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	private String email;

    private String layer_name;
    
    private Integer id_feature;

    private String tipo_incidencia;
    
    private String gravedad_incidencia;
    
    private String descripcion;    
    
	public LocalgisIncidencia() {
	}

	public Integer getIncidenciaId() {
		return incidenciaId;
	}

	public void setIncidenciaId(Integer incidenciaId) {
		this.incidenciaId = incidenciaId;
	}

	public Integer getId_municipio() {
		return id_municipio;
	}

	public void setId_municipio(Integer idMunicipio) {
		id_municipio = idMunicipio;
	}

	public Integer getMapid() {
		return mapid;
	}

	public void setMapid(Integer mapid) {
		this.mapid = mapid;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLayer_name() {
		return layer_name;
	}

	public void setLayer_name(String layer_name) {
		this.layer_name = layer_name;
	}

	public Integer getId_feature() {
		return id_feature;
	}

	public void setId_feature(Integer idFeature) {
		id_feature = idFeature;
	}

	public String getTipo_incidencia() {
		return tipo_incidencia;
	}

	public void setTipo_incidencia(String tipoIncidencia) {
		tipo_incidencia = tipoIncidencia;
	}

	public String getGravedad_incidencia() {
		return gravedad_incidencia;
	}

	public void setGravedad_incidencia(String gravedadIncidencia) {
		gravedad_incidencia = gravedadIncidencia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
