/**
 * EntidadesAgrupadasEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class EntidadesAgrupadasEIEL extends WorkflowEIEL implements Serializable, EIELPanel{

	public EntidadesAgrupadasEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Entidad","codEntidad","eiel_t_entidades_agrupadas","getCodEntidad"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Poblamiento","codNucleo","eiel_t_entidades_agrupadas","getCodNucleo"));
	}	
			
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codEntidad", codEntidad);
		fields.put("codNucleo", codNucleo);
		return fields;
	}
	private String codEntidad = null;
	private String codNucleo = null;
	private String codEntidad_agrupada = null;
	private String codNucleo_agrupado=null;
	private VersionEiel version = null;
	

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

	public String getCodEntidad() {
		return codEntidad;
	}

	public void setCodINEEntidad(String codEntidad) {
		this.codEntidad = codEntidad;
	}

	public String getCodNucleo() {
		return codNucleo;
	}

	public void setCodINENucleo(String codNucleo) {
		this.codNucleo = codNucleo;
	}

	public String getCodEntidad_agrupada() {
		return codEntidad_agrupada;
	}

	public void setCodINEEntidad_agrupada(String codEntidad_agrupada) {
		this.codEntidad_agrupada = codEntidad_agrupada;
	}

	public String getCodNucleo_agrupado() {
		return codNucleo_agrupado;
	}

	public void setCodINENucleo_agrupado(String codNucleo_agrupado) {
		this.codNucleo_agrupado = codNucleo_agrupado;
	}

	@Override
	public String toString() {
		return "EntidadesAgrupadasEIEL [codINEMunicipio="+codINEMunicipio+",codINEEntidad=" + codEntidad
				+ ", codINEPoblamiento=" + codNucleo + "]";
	}
}
