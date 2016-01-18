/**
 * DeterminacionPanelBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.panels.tramite;

import java.util.Arrays;

import com.gestorfip.app.planeamiento.beans.tramite.OperacionDeterminacionBean;


public class DeterminacionPanelBean implements Comparable {
	
	private int id;
	private String codigo;
	private int caracter;
	private String apartado;
	private String nombre;
	private String etiqueta;
	private boolean suspendida;
	private String texto;
	private int unidad_determinacionid;
	private int base_determinacionid;
	private int madre;
	private int tramite;
	
	private boolean nueva = false;
	private boolean eliminada = false;
	private boolean modificada = false;
	
	// si es una determinacion asociada a un tramite de catalogo sistematizado no se puede modificar
	// ni eliminar
	private boolean isModificable = false;
	
	private boolean aplicableEntidad = false;

	public boolean isAplicableEntidad() {
		return aplicableEntidad;
	}

	public void setAplicableEntidad(boolean aplicableEntidad) {
		this.aplicableEntidad = aplicableEntidad;
	}

	private DeterminacionPanelBean[] lstDeterminacionesHijas ;
	private UnidadDeterminacionPanelBean unidadPanelBean;
	private BaseDeterminacionPanelBean basePanelBean;
	private ValoresReferenciaPanelBean valoresReferenciaPanelBean;
	private GrupoAplicacionPanelBean grupoAplicacionPanelBean;
	private DeterminacionesReguladorasPanelBean determinacionesReguladorasPanelBean;
	private RegulacionesEspecificasPanelBean[] lstRegulacionesEspecificaPanelBean;
	private DocumentosPanelBean lstDocumentosPanelBean;
	private OperacionDeterminacionBean[] lstOperacionDeterminacion;
	
	
	public OperacionDeterminacionBean[] getLstOperacionDeterminacion() {
		return lstOperacionDeterminacion;
	}

	public void setLstOperacionDeterminacion(
			OperacionDeterminacionBean[] lstOperacionDeterminacion) {
		this.lstOperacionDeterminacion = lstOperacionDeterminacion;
	}

	public RegulacionesEspecificasPanelBean[] getRegulacionEspecificaPanelBean() {
		return lstRegulacionesEspecificaPanelBean;
	}

	public void setRegulacionEspecificaPanelBean(
			RegulacionesEspecificasPanelBean[] lstRegulacionesEspecificaPanelBean) {
		this.lstRegulacionesEspecificaPanelBean = lstRegulacionesEspecificaPanelBean;
	}

	public GrupoAplicacionPanelBean getGrupoAplicacionPanelBean() {
		return grupoAplicacionPanelBean;
	}

	public void setGrupoAplicacionPanelBean(
			GrupoAplicacionPanelBean grupoAplicacionPanelBean) {
		this.grupoAplicacionPanelBean = grupoAplicacionPanelBean;
	}
	
	public DeterminacionesReguladorasPanelBean getDeterminacionesReguladorasPanelBean() {
		return determinacionesReguladorasPanelBean;
	}

	public void setDeterminacionesReguladorasPanelBean(
			DeterminacionesReguladorasPanelBean determinacionesReguladorasPanelBean) {
		this.determinacionesReguladorasPanelBean = determinacionesReguladorasPanelBean;
	}

	public ValoresReferenciaPanelBean getValoresReferenciaPanelBean() {
		return valoresReferenciaPanelBean;
	}

	public void setValoresReferenciaPanelBean(ValoresReferenciaPanelBean valoresReferenciaPanelBean) {
		this.valoresReferenciaPanelBean = valoresReferenciaPanelBean;
	}
	

	public BaseDeterminacionPanelBean getBaseBean() {
		return basePanelBean;
	}

	public void setBaseBean(BaseDeterminacionPanelBean basePanelBean) {
		this.basePanelBean = basePanelBean;
	}

	public UnidadDeterminacionPanelBean getUnidadBean() {
		return unidadPanelBean;
	}

	public void setUnidadBean(UnidadDeterminacionPanelBean unidadPanelBean) {
		this.unidadPanelBean = unidadPanelBean;
	}
	
	
	
	
	public DeterminacionPanelBean[] getLstDeterminacionesHijas() {
		return lstDeterminacionesHijas;
	}
	
	public void setLstDeterminacionesHijas(
			DeterminacionPanelBean[] lstDeterminacionesHijas) {
		this.lstDeterminacionesHijas = lstDeterminacionesHijas;
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
	
	public int getCaracter() {
		return caracter;
	}
	
	public void setCaracter(int caracter) {
		this.caracter = caracter;
	}
	
	public String getApartado() {
		return apartado;
	}
	
	public void setApartado(String apartado) {
		this.apartado = apartado;
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
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public int getUnidad_determinacionid() {
		return unidad_determinacionid;
	}
	
	public void setUnidad_determinacionid(int unidadDeterminacionid) {
		unidad_determinacionid = unidadDeterminacionid;
	}
	
	public int getBase_determinacionid() {
		return base_determinacionid;
	}
	
	public void setBase_determinacionid(int baseDeterminacionid) {
		base_determinacionid = baseDeterminacionid;
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
	

	public void addDeterminacionHija(DeterminacionPanelBean determinacionHija) {
		if(lstDeterminacionesHijas == null){
			
			lstDeterminacionesHijas = new DeterminacionPanelBean[1];
			lstDeterminacionesHijas[0] = determinacionHija;
		
		}
		else{
			lstDeterminacionesHijas = (DeterminacionPanelBean[]) Arrays.copyOf(lstDeterminacionesHijas, 
																			lstDeterminacionesHijas.length+1);
		
			lstDeterminacionesHijas[lstDeterminacionesHijas.length-1] = determinacionHija;
		}
	}
	
	public void addUnidadDeterminacion(DeterminacionPanelBean determinacion) {
			
		unidadPanelBean = new UnidadDeterminacionPanelBean();
		unidadPanelBean.setDeterminacion(determinacion);

	
	}
	
	public void addBaseDeterminacion(DeterminacionPanelBean determinacion) {
		
		basePanelBean = new BaseDeterminacionPanelBean();
		basePanelBean.setDeterminacion(determinacion);

	}
	public DocumentosPanelBean getLstDocumentosPanelBean() {
		return lstDocumentosPanelBean;
	}

	public void setLstDocumentosPanelBean(
			DocumentosPanelBean lstDocumentosPanelBean) {
		this.lstDocumentosPanelBean = lstDocumentosPanelBean;
	}

	

	public boolean isNueva() {
		return nueva;
	}

	public void setNueva(boolean determinacionNueva) {
		this.nueva = determinacionNueva;
	}
	
	public boolean isEliminada() {
		return eliminada;
	}

	public void setEliminada(boolean determinacionEliminada) {
		this.eliminada = determinacionEliminada;
	}
	

	public boolean isModificada() {
		return modificada;
	}

	public void setModificada(boolean modificada) {
		this.modificada = modificada;
	}
	

	public boolean isModificable() {
		return isModificable;
	}

	public void setModificable(boolean isModificable) {
		this.isModificable = isModificable;
	}

	public int compareTo(Object o) {

		DeterminacionPanelBean dbp = (DeterminacionPanelBean) o;
		
		return apartado.compareToIgnoreCase(dbp.getApartado());

	}

}
