/**
 * EntidadBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.tramite;

import java.util.Arrays;


public class EntidadBean {
	
	private int id;
	private String codigo;
	private String clave;
	private String nombre;
	private String etiqueta;
	private boolean suspendida;

	private int base_entidadid;
	private int madre;
	private int tramite;
	private boolean isModificable = false;
	
	private String codigoValRefCU;

	private int idLayer = -1;
	private int idFeature = -1;
	private String geometria = null;
	
	private EntidadBean[] lstEntidadesHijas ;
	private BaseEntidadBean baseBean;
	
	private DocumentoEntidadBean[] lstDocumento;
	private DocumentoEntidadBean[] lstDocumentoAlta;
	private DocumentoEntidadBean[] lstDocumentoBaja;
	
	private OperacionEntidadBean[] lstOperacionEntidad;

	public OperacionEntidadBean[] getLstOperacionEntidad() {
		return lstOperacionEntidad;
	}

	public void setLstOperacionEntidad(OperacionEntidadBean[] lstOperacionEntidad) {
		this.lstOperacionEntidad = lstOperacionEntidad;
	}

	public DocumentoEntidadBean[] getLstDocumento() {
		return lstDocumento;
	}

	public void setLstDocumento(DocumentoEntidadBean[] lstDocumento) {
		this.lstDocumento = lstDocumento;
	}

	public DocumentoEntidadBean[] getLstDocumentoAlta() {
		return lstDocumentoAlta;
	}

	public void setLstDocumentoAlta(DocumentoEntidadBean[] lstDocumentoAlta) {
		this.lstDocumentoAlta = lstDocumentoAlta;
	}

	public DocumentoEntidadBean[] getLstDocumentoBaja() {
		return lstDocumentoBaja;
	}

	public void setLstDocumentoBaja(DocumentoEntidadBean[] lstDocumentoBaja) {
		this.lstDocumentoBaja = lstDocumentoBaja;
	}

	public BaseEntidadBean getBaseBean() {
		return baseBean;
	}

	public void setBaseBean(BaseEntidadBean baseBean) {
		this.baseBean = baseBean;
	}
	
	public EntidadBean[] getLstEntidadesHijas() {
		return lstEntidadesHijas;
	}
	
	public void setLstEntidadesHijas(
			EntidadBean[] lstEntidadesHijas) {
		this.lstEntidadesHijas = lstEntidadesHijas;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}
	
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	public boolean isSuspendida() {
		return suspendida;
	}
	
	public void setSuspendida(boolean suspendida) {
		this.suspendida = suspendida;
	}
	
	public int getBase_entidadid() {
		return base_entidadid;
	}
	
	public void setBase_entidadid(int baseEntidadid) {
		base_entidadid = baseEntidadid;
	}
	
	public int getMadre() {
		return madre;
	}
	
	public void setMadre(int madre) {
		this.madre = madre;
	}
	
	public int getTramite() {
		return tramite;
	}
	
	public void setTramite(int tramite) {
		this.tramite = tramite;
	}
	

	public void addDeterminacionHija(EntidadBean determinacionHija) {
		if(lstEntidadesHijas == null){
			
			lstEntidadesHijas = new EntidadBean[1];
			lstEntidadesHijas[0] = determinacionHija;
		
		}
		else{
			lstEntidadesHijas = (EntidadBean[]) Arrays.copyOf(lstEntidadesHijas, 
																			lstEntidadesHijas.length+1);
		
			lstEntidadesHijas[lstEntidadesHijas.length-1] = determinacionHija;
		}
	}

	public void addBaseDeterminacion(EntidadBean entidad) {
		
		baseBean = new BaseEntidadBean();
		baseBean.setEntidadid(entidad.getBase_entidadid());
		baseBean.setTramite(entidad.getTramite());
		baseBean.setId(entidad.getId());
		baseBean.setNombre(entidad.getNombre());

	}
	
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public int getIdLayer() {
		return idLayer;
	}

	public void setIdLayer(int idLayer) {
		this.idLayer = idLayer;
	}

	public int getIdFeature() {
		return idFeature;
	}

	public void setIdFeature(int idFeature) {
		this.idFeature = idFeature;
	}
	
	public String getGeometria() {
		return geometria;
	}

	public void setGeometria(String geometria) {
		this.geometria = geometria;
	}
	
	public boolean isModificable() {
		return isModificable;
	}

	public void setModificable(boolean isModificable) {
		this.isModificable = isModificable;
	}
	

	public String getCodigoValRefCU() {
		return codigoValRefCU;
	}

	public void setCodigoValRefCU(String codigoValRefCU) {
		this.codigoValRefCU = codigoValRefCU;
	}

}
