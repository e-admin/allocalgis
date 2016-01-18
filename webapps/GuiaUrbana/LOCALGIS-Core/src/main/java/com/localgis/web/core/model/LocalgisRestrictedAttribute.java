/**
 * LocalgisRestrictedAttribute.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

import com.localgis.web.core.ConstantesSQL;

public class LocalgisRestrictedAttribute {

	private Integer layeridgeopista;
	private Integer identidad;
	private Integer attributeidgeopista;
	private Short   mappublic;
	private Integer idalias;
	private String  alias;
		
	public LocalgisRestrictedAttribute() {
    }
	
	public LocalgisRestrictedAttribute(Integer layeridgeopista, Integer identidad, Integer attributeidgeopista, Short mappublic, Integer idalias, String alias) {
        super();
        this.layeridgeopista = layeridgeopista;
        this.identidad = identidad;
        this.attributeidgeopista = attributeidgeopista;
        this.mappublic = mappublic;
        this.idalias = idalias;
        this.alias = alias;
    }

    public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getLayeridgeopista() {
		return layeridgeopista;
	}
	
	public void setLayeridgeopista(Integer layeridgeopista) {
		this.layeridgeopista = layeridgeopista;
	}
	

	
	public Short getMappublic() {
		return mappublic;
	}
	
	/*
	 * El metodo no es sobrecargado para evitar problemas con ibatis a la hora de devolver un resultMap
	 */
    public void setMappublicBoolean(Boolean mappublic) {
        if (mappublic.booleanValue()) {
            this.mappublic = ConstantesSQL.TRUE;
        } else {
            this.mappublic = ConstantesSQL.FALSE;
        }
    }

	public Integer getAttributeidgeopista() {
		return attributeidgeopista;
	}

	public void setAttributeidgeopista(Integer attributeidgeopista) {
		this.attributeidgeopista = attributeidgeopista;
	}

	public void setMappublic(Short mappublic) {
		this.mappublic = mappublic;
	}

	public Integer getIdalias() {
		return idalias;
	}

	public void setIdalias(Integer idalias) {
		this.idalias = idalias;
	}

	public Integer getIdentidad() {
		return identidad;
	}

	public void setIdentidad(Integer idmunicipio) {
		this.identidad = idmunicipio;
	}

}
