/**
 * NumeroPolicia.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.contaminantes;

import java.sql.Date;

import com.vividsolutions.jump.util.java2xml.XMLBinder.CustomConverter;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 18-nov-2004
 * Time: 16:49:32
 */
public class NumeroPolicia {
	
    private String id = null;
    private String id_via = null;
    private String tipovia = null;
    private String nombrevia = null;
    private String rotulo = null;
    
    private String idalp = null;
    private String calificador = null;
    private String numero = null;
    private Date fechaEjecucion = null;
    private Integer codigoINEMunicipio = null;

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_via() {
        return id_via;
    }

    public void setId_via(String id_via) {
        this.id_via = id_via;
    }

    public String getNombrevia() {
        return nombrevia;
    }
 
    public void setNombrevia(String nombrevia) {
        this.nombrevia = nombrevia;
    }

    public String getTipovia() {
        return tipovia;
    }

    public void setTipovia(String tipovia) {
        this.tipovia = tipovia;
    }
    public String toString()
    {
        return (nombrevia!=null?nombrevia+" ":"") +(rotulo!=null?rotulo+" ":"");
    }

	public String getIdalp() {
		return idalp;
	}

	public void setIdalp(String idalp) {
		this.idalp = idalp;
	}

	public String getCalificador() {
		return calificador;
	}

	public void setCalificador(String calificador) {
		this.calificador = calificador;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}

	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	public Integer getCodigoINEMunicipio() {
		return codigoINEMunicipio;
	}

	public void setCodigoINEMunicipio(Integer codigoINEMunicipio) {
		this.codigoINEMunicipio = codigoINEMunicipio;
	}
	
	public static CustomConverter getDateCustomConverter()
	{
		return new CustomConverter() {
			public Object toJava(String value) {
				Date mapTime = new Date(Long.parseLong(value));
				return mapTime;

			}

			public String toXML(Object object) {
				Date actualTime = (Date)object;
				long actualMilliseconds = actualTime.getTime();
				return String.valueOf(actualMilliseconds);
			}
		};
	}
}
