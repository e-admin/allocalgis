/**
 * TramosConduccionEIEL.java
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

public class TramosConduccionEIEL extends WorkflowEIEL implements Serializable, EIELPanel {

	public TramosConduccionEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código provincia","codprov","eiel_t_abast_tcn","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_abast_tcn","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_t_abast_tcn","getClave"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Tramo","tramo_cn","eiel_t_abast_tcn","getTramo_cn"));
		//relacionFields.add(new LCGCampoCapaTablaEIEL("Pmi","pmi","eiel_t_abast_tcn","getPmi"));
		//relacionFields.add(new LCGCampoCapaTablaEIEL("Pmf","pmf","eiel_t_abast_tcn","getPmf"));
	}	

	@Override
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();	
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("clave", clave);
		fields.put("tramo_cn", tramo_cn);	
		//fields.put("pmi", pmi);		
		//fields.put("pmf", pmf);			
		return fields;
	}	
	
	private String clave = null;
	private String tramo_cn = null;
	
	private String titular = null;
	private String gestor = null;
	private String estado = null;
	private String material = null;
	private String observaciones = null;
	private String sist_trans=null;
	
	private Float longitud = null;
	private Float pmi = null;
	private Float pmf = null;
	
	private Integer diametro = null;
	
	private Date fechaInstalacion = null;
	private Date fecha_revision = null;
	private Integer estado_revision=null;

	
	private VersionEiel version = null;
	
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
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
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Float getPmf() {
		return pmf;
	}
	public void setPmf(Float pmf) {
		this.pmf = pmf;
	}
	public Float getPmi() {
		return pmi;
	}
	public void setPmi(Float pmi) {
		this.pmi = pmi;
	}
	public Float getLongitud() {
		return longitud;
	}
	public void setLongitud(Float longitud) {
		this.longitud = longitud;
	}
	public Integer getDiametro() {
		return diametro;
	}
	public void setDiametro(Integer diametro) {
		this.diametro = diametro;
	}
	public Date getFechaInstalacion() {
		return fechaInstalacion;
	}
	public void setFechaInstalacion(Date fechaInstalacion) {
		this.fechaInstalacion = fechaInstalacion;
	}
	public String getTramo_cn() {
		return tramo_cn;
	}
	public void setTramo_cn(String tramo_cn) {
		this.tramo_cn = tramo_cn;
	}
	public String getTitular() {
		return titular;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}
	public String getGestor() {
		return gestor;
	}
	public void setGestor(String gestor) {
		this.gestor = gestor;
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
	public Date getFecha_revision() {
		return fecha_revision;
	}
	public void setFecha_revision(Date fecha_revision) {
		this.fecha_revision = fecha_revision;
	}
	public Integer getEstado_revision() {
		return estado_revision;
	}
	public void setEstado_revision(Integer estado_revision) {
		this.estado_revision = estado_revision;
	}
	public String getSist_trans() {
		return sist_trans;
	}
	public void setSist_trans(String sist_trans) {
		this.sist_trans = sist_trans;
	}
}
