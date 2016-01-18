/**
 * EntidadPanelBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.panels.tramite;

import java.util.Arrays;

import com.gestorfip.app.planeamiento.beans.tramite.OperacionEntidadBean;


public class EntidadPanelBean implements Comparable {
	
	private int id;
	private String codigo;
	private String clave;
	private String nombre;
	private String etiqueta;
	private boolean suspendida;
	private int base_entidadid;
	
	private int madre;
	private int tramite;
	
	private boolean nueva = false;
	private boolean eliminada = false;
	private boolean modificada = false;
	
	
	private String codigoValRefCU;


	// si es una entidad asociada a un tramite de catalogo sistematizado no se puede modificar
	// ni eliminar
	private boolean isModificable = false;

	
	private int idLayer = -1;
	private int idFeature = -1;

	private EntidadPanelBean[] lstEntidadesHijas ;

	private BaseEntidadPanelBean basePanelBean;
	private DocumentosPanelBean lstDocumentosPanelBean;
	private OperacionEntidadBean[] lstOperacionEntidad;


	public OperacionEntidadBean[] getLstOperacionEntidad() {
		return lstOperacionEntidad;
	}

	public void setLstOperacionEntidad(OperacionEntidadBean[] lstOperacionEntidad) {
		this.lstOperacionEntidad = lstOperacionEntidad;
	}

	public DocumentosPanelBean getLstDocumentosPanelBean() {
		return lstDocumentosPanelBean;
	}

	public void setLstDocumentosPanelBean(DocumentosPanelBean lstDocumentosPanelBean) {
		this.lstDocumentosPanelBean = lstDocumentosPanelBean;
	}

	public BaseEntidadPanelBean getBaseBean() {
		return basePanelBean;
	}

	public void setBaseBean(BaseEntidadPanelBean basePanelBean) {
		this.basePanelBean = basePanelBean;
	}

	
	public EntidadPanelBean[] getLstEntidadesHijas() {
		return lstEntidadesHijas;
	}
	
	public void setLstEntidadesHijas(
			EntidadPanelBean[] lstEntidadesHijas) {
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
	
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}


	public void addEntidadHija(EntidadPanelBean entidadHija) {
		if(lstEntidadesHijas == null){
			
			lstEntidadesHijas = new EntidadPanelBean[1];
			lstEntidadesHijas[0] = entidadHija;
		
		}
		else{
			lstEntidadesHijas = (EntidadPanelBean[]) Arrays.copyOf(lstEntidadesHijas, 
																			lstEntidadesHijas.length+1);
		
			lstEntidadesHijas[lstEntidadesHijas.length-1] = entidadHija;
		}
	}
	
	public void addBaseEntidad(EntidadPanelBean entidad) {
		
		basePanelBean = new BaseEntidadPanelBean();
		basePanelBean.setEntidad(entidad);

	}
	

	public boolean isNueva() {
		return nueva;
	}

	public void setNueva(boolean entidadNueva) {
		this.nueva = entidadNueva;
	}
	
	public boolean isEliminada() {
		return eliminada;
	}

	public void setEliminada(boolean entidadEliminada) {
		this.eliminada = entidadEliminada;
	}
	

	public boolean isModificada() {
		return modificada;
	}

	public void setModificada(boolean modificada) {
		this.modificada = modificada;
	}
	
	public int getBase_entidadid() {
		return base_entidadid;
	}

	public void setBase_entidadid(int baseEntidadid) {
		base_entidadid = baseEntidadid;
	}

	public BaseEntidadPanelBean getBasePanelBean() {
		return basePanelBean;
	}

	public void setBasePanelBean(BaseEntidadPanelBean basePanelBean) {
		this.basePanelBean = basePanelBean;
	}

	public int getIdFeature() {
		return idFeature;
	}

	public void setIdFeature(int idFeature) {
		this.idFeature = idFeature;
	}
	
	public int getIdLayer() {
		return idLayer;
	}

	public void setIdLayer(int idLayer) {
		this.idLayer = idLayer;
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


	public int compareTo(Object o) {

		EntidadPanelBean dbp = (EntidadPanelBean) o;
		
		return clave.compareToIgnoreCase(dbp.getClave());

	}
}
