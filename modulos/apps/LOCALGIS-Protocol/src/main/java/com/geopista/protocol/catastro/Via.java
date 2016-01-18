/**
 * Via.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.catastro;

import java.sql.Date;

import com.vividsolutions.jump.util.java2xml.XMLBinder.CustomConverter;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 22-dic-2004
 * Time: 13:25:38
 */
public class Via {

    String id;
    String codigoIne;
    String codigoCatastro;
    String tipoViaNormalizadoCatastro;
    String tipoViaIne;
    String nombreViaIne;
    String nombreViaCortoIne;
    String nombreCatastro;
    Float length;
    
    String idalp = null;
    Date fechaGrabacionAyto = null;
    Date fechaGrabacionCierre = null;
    Date fechaEjecucion = null;
    
    String municipio = null;
    Integer idMunicipio = null;
    Integer idProvincia = null;
    

    public Integer getIdProvincia() {
		return idProvincia;
	}

	public void setIdProvincia(Integer idProvincia) {
		this.idProvincia = idProvincia;
	}

	public String getCodigoIne() {
		return codigoIne;
	}

	public void setCodigoIne(String codigoIne) {
		this.codigoIne = codigoIne;
	}

	public String getIdalp() {
		return idalp;
	}

	public void setIdalp(String idalp) {
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

    public void setCogigoIne(String ine){
        this.codigoIne= ine;
    }

    public void setCodigoCatastro(String codCatastro){
        this.codigoCatastro= codCatastro;
    }

    public Integer getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
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

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

}
