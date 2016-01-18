/**
 * ColectorEIEL.java
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

public class ColectorEIEL extends WorkflowEIEL implements Serializable, EIELPanel {
 
	public ColectorEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código provincia","codprov","eiel_t_saneam_tcl","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_saneam_tcl","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_t_saneam_tcl","getClave"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Tramo","tramo_cl","eiel_t_saneam_tcl","getCodOrden"));
	}	
	
	@Override
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("clave", clave);
		fields.put("tramo_cl", codOrden);		
		return fields;
	}
	
	private String clave = null;
	private String codOrden = null;
	

	private String codentidad_Pobl = null;
	private String codPoblamiento_Pobl = null;
	private String observaciones = null;
	private Date fechaRevision = null;
	private Integer estado_revision = null;
	private String titularidad=null;
	private String gestion=null;
	private String estado=null;
	private String material=null;
	private String tipo_red=null;
	private String tip_interceptor=null;
	private Date fecha_inst=null;
	private String sist_impulsion=null;


	private VersionEiel version = null;
	
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	public String getCodOrden() {
		return codOrden;
	}
	public void setCodOrden(String codOrden) {
		this.codOrden = codOrden;
	}
	
	public String getCodEntidad_Pobl() {
		return codentidad_Pobl;
	}
	public void setCodEntidad_Pobl(String codentidad_Pobl) {
		this.codentidad_Pobl = codentidad_Pobl;
	}
	public String getCodPoblamiento_Pobl() {
		return codPoblamiento_Pobl;
	}
	public void setCodPoblamiento_Pobl(String codPoblamiento_Pobl) {
		this.codPoblamiento_Pobl = codPoblamiento_Pobl;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Date getFechaRevision() {
		return fechaRevision;
	}
	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}
	public Integer getEstado_Revision() {
		return estado_revision;
	}
	public void setEstado_Revision(Integer estado_revision) {
		this.estado_revision = estado_revision;
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
	
	public String getTitularidad() {
		return titularidad;
	}
	public void setTitularidad(String titularidad) {
		this.titularidad = titularidad;
	}
	public String getGestion() {
		return gestion;
	}
	public void setGestion(String gestion) {
		this.gestion = gestion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getTipo_red() {
		return tipo_red;
	}
	public void setTipo_red(String tipo_red) {
		this.tipo_red = tipo_red;
	}
	public String getTip_interceptor() {
		return tip_interceptor;
	}
	public void setTip_interceptor(String tip_interceptor) {
		this.tip_interceptor = tip_interceptor;
	}
	public Date getFecha_inst() {
		return fecha_inst;
	}
	public void setFecha_inst(Date fecha_inst) {
		this.fecha_inst = fecha_inst;
	}
	public String getSist_impulsion() {
		return sist_impulsion;
	}
	public void setSist_impulsion(String sist_impulsion) {
		this.sist_impulsion = sist_impulsion;
	}
	
}
