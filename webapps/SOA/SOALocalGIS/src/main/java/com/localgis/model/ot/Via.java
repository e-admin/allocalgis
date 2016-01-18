/**
 * Via.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.ot;

import java.io.Serializable;
import java.sql.Date;

import com.vividsolutions.jump.util.java2xml.XMLBinder.CustomConverter;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 22-dic-2004
 * Time: 13:25:38
 */
public class Via{

    private String id = null;
    private String codigoIne = null;
    private String codigoCatastro = null;
    private String tipoViaNormalizadoCatastro = null;
    private String tipoViaIne = null;
    private String nombreViaIne = null;
    private String nombreViaCortoIne = null;
    private String nombreCatastro = null;
    private Float length = null;
    
    private Integer idalp = null;
    private Date fechaGrabacionAyto = null;
    private Date fechaGrabacionCierre = null;
    private Date fechaEjecucion = null;
    private Integer codigoINEMunicipio = null;
    

    public String getCodigoIne() {
		return codigoIne;
	}

	public void setCodigoIne(String codigoIne) {
		this.codigoIne = codigoIne;
	}

	public Integer getIdalp() {
		return idalp;
	}

	public void setIdalp(Integer idalp) {
		this.idalp = idalp;
	}

	public Date getFechaGrabacionAyto() {
		return fechaGrabacionAyto;
	}

	public void setFechaGrabacionAyto(Date fechaGrabacionAyto) {
		this.fechaGrabacionAyto = fechaGrabacionAyto;
	}

	public Date getFechaGrabacionCierre() {
		return fechaGrabacionCierre;
	}

	public void setFechaGrabacionCierre(Date fechaGrabacionCierre) {
		this.fechaGrabacionCierre = fechaGrabacionCierre;
	}

	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}

	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	public void setId(String id){
        this.id= id;
    }

    public String getId(){
        return id;
    }
    
    public void setCodigoCatastro(String codCatastro){
        this.codigoCatastro= codCatastro;
    }

    public String getCodigoCatastro(){
        return codigoCatastro;
    }

    public void setTipoViaNormalizadoCatastro(String tipo){
        this.tipoViaNormalizadoCatastro= tipo;
    }

    public String getTipoViaNormalizadoCatastro(){
        return tipoViaNormalizadoCatastro;
    }

    public void setTipoViaIne(String tipo){
        this.tipoViaIne= tipo;
    }

    public String getTipoViaIne(){
        return tipoViaIne;
    }

    public void setNombreViaIne(String nombre){
        this.nombreViaIne= nombre;
    }

    public String getNombreViaIne(){
        return nombreViaIne;
    }

    public void setNombreViaCortoIne(String nombre){
        this.nombreViaCortoIne= nombre;
    }

    public String getNombreViaCortoIne(){
        return nombreViaCortoIne;
    }

    public void setNombreCatastro(String nombre){
        this.nombreCatastro= nombre;
    }

    public String getNombreCatastro(){
        return nombreCatastro;
    }

    public void setLength(Float lon){
        this.length= lon;
    }

    public Float getLength(){
        return length;
    }

	public Integer getCodigoINEMunicipio() {
		return codigoINEMunicipio;
	}

	public void setCodigoINEMunicipio(Integer codigoINEMunicipio) {
		this.codigoINEMunicipio = codigoINEMunicipio;
	}
	
	public static CustomConverter getDateCustonConverter()
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
