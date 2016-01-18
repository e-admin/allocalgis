/**
 * TramitePanelBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.panels.tramite;

import com.gestorfip.app.planeamiento.beans.diccionario.CaracteresDeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.diccionario.TipoTramiteBean;
import com.gestorfip.app.planeamiento.beans.tramite.DocumentosBean;



public class TramitePanelBean {

	
	private TipoTramiteBean tipoTramite;
	private String codigo;
	private String texto;
	private int id;
	private int fip;
	private CaracteresDeterminacionPanelBean[] lstCaracteresDeterminacion;

	private DeterminacionPanelBean[] lstDeterminacionesPanelBean;
	private EntidadPanelBean[] lstEntidadesPanelBean;
	
	
	private DocumentosBean[] lstDocumentosBean;
	
	
	
	public DocumentosBean[] getLstDocumentosBean() {
		return lstDocumentosBean;
	}

	public void setLstDocumentosBean(
			DocumentosBean[] lstDocumentosBean) {
		this.lstDocumentosBean = lstDocumentosBean;
	}

	public EntidadPanelBean[] getLstEntidadesPanelBean() {
		return lstEntidadesPanelBean;
	}

	public void setLstEntidadesPanelBean(EntidadPanelBean[] lstEntidadesPanelBean) {
		this.lstEntidadesPanelBean = lstEntidadesPanelBean;
	}

	public CaracteresDeterminacionPanelBean[] getLstCaracteresDeterminacion() {
		return lstCaracteresDeterminacion;
	}

	public void setLstCaracteresDeterminacion(
			CaracteresDeterminacionPanelBean[] lstCaracteresDeterminacion) {
		this.lstCaracteresDeterminacion = lstCaracteresDeterminacion;
	}


	public DeterminacionPanelBean[] getLstDeterminacionesPanelBean() {
		return lstDeterminacionesPanelBean;
	}

	public void setLstDeterminacionesPanelBean(DeterminacionPanelBean[] lstDeterminacionesPanelBean) {
		this.lstDeterminacionesPanelBean = lstDeterminacionesPanelBean;
	}

	public TipoTramiteBean getTipoTramite() {
		return tipoTramite;
	}
	
	public void setTipoTramite(TipoTramiteBean tipoTramite) {
		this.tipoTramite = tipoTramite;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getFip() {
		return fip;
	}

	public void setFip(int fip) {
		this.fip = fip;
	}

}
