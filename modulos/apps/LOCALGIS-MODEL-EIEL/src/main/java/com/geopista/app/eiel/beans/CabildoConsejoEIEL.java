/**
 * CabildoConsejoEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class CabildoConsejoEIEL extends WorkflowEIEL implements Serializable, EIELPanel{
	
	public CabildoConsejoEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_cabildo_consejo","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Isla","cod_isla","eiel_t_cabildo_consejo","getCodIsla"));
	}	

	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("cod_isla", codIsla);
		return fields;
	}

	private String codINEProvincia = null;
	private String codIsla = null;
	private String denominacion = null;

	
	private VersionEiel version = null;
	
	
	public String getCodIsla() {
		return codIsla;
	}
	public void setCodIsla(String codIsla) {
		this.codIsla = codIsla;
	}
	public String getDenominacion() {
		return denominacion;
	}
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(VersionEiel version) {
		this.version = version;
	}
	/**
	 * @return the version
	 */
	public VersionEiel getVersion() {
		return version;
	}
	
	
	
}
