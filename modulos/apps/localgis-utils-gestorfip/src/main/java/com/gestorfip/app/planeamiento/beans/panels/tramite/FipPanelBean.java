/**
 * FipPanelBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.panels.tramite;

import java.util.Date;

import com.gestorfip.app.planeamiento.beans.tramite.DocumentosBean;

public class FipPanelBean {
	
	  private int id; 
	  private String pais; 
	  private String version; 
	  private String srs; 
	  private String fecha;
	  private String fechaConsolidacion;
	  private int idAmbito; 
	  
	  private String municipio;

	private TramitePanelBean tramiteCatalogoSistematizado;
	  private TramitePanelBean tramitePlaneamientoVigente;
	  private TramitePanelBean tramitePlaneamientoEncargado;
	  
	  private DeterminacionPanelBean[] lstArbolDeterminacionesCatalogoSistematizado;
	  private DeterminacionPanelBean[] lstArbolDeterminacionesPlaneamientoEncargado;
	  private DeterminacionPanelBean[] lstArbolDeterminacionesPlaneamientoVigente;
	  
	 
		private EntidadPanelBean[] lstArbolEntidadesCatalogoSistematizado;	
		private EntidadPanelBean[] lstArbolEntidadesPlaneamientoVigente;
		private EntidadPanelBean[] lstArbolEntidadesPlaneamientoEncargado;
	 
	  private DocumentosBean[] lstDocumentos;

	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getPais() {
		return pais;
	}
	
	public void setPais(String pais) {
		this.pais = pais;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getSrs() {
		return srs;
	}
	
	public void setSrs(String srs) {
		this.srs = srs;
	}
	
	public TramitePanelBean getTramitePlaneamientoVigente() {
		return tramitePlaneamientoVigente;
	}
	
	public void setTramitePlaneamientoVigente(
				TramitePanelBean tramitePlaneamientoVigente) {
		this.tramitePlaneamientoVigente = tramitePlaneamientoVigente;
	}

		
	public TramitePanelBean getTramiteCatalogoSistematizado() {
		return tramiteCatalogoSistematizado;
	}
	
	public void setTramiteCatalogoSistematizado(
			TramitePanelBean tramiteCatalogoSistematizado) {
		this.tramiteCatalogoSistematizado = tramiteCatalogoSistematizado;
	}
	
	public TramitePanelBean getTramitePlaneamientoEncargado() {
		return tramitePlaneamientoEncargado;
	}
	
	public void setTramitePlaneamientoEncargado(
			TramitePanelBean tramitePlaneamientoEncargado) {
		this.tramitePlaneamientoEncargado = tramitePlaneamientoEncargado;
	}

	

	public EntidadPanelBean[] getLstArbolEntidadesCatalogoSistematizado() {
		return lstArbolEntidadesCatalogoSistematizado;
	}

	public void setLstArbolEntidadesCatalogoSistematizado(
			EntidadPanelBean[] lstArbolEntidadesCatalogoSistematizado) {
		this.lstArbolEntidadesCatalogoSistematizado = lstArbolEntidadesCatalogoSistematizado;
	}

	public EntidadPanelBean[] getLstArbolEntidadesPlaneamientoVigente() {
		return lstArbolEntidadesPlaneamientoVigente;
	}

	public void setLstArbolEntidadesPlaneamientoVigente(
			EntidadPanelBean[] lstArbolEntidadesPlaneamientoVigente) {
		this.lstArbolEntidadesPlaneamientoVigente = lstArbolEntidadesPlaneamientoVigente;
	}

	public EntidadPanelBean[] getLstArbolEntidadesPlaneamientoEncargado() {
		return lstArbolEntidadesPlaneamientoEncargado;
	}

	public void setLstArbolEntidadesPlaneamientoEncargado(
			EntidadPanelBean[] lstArbolEntidadesPlaneamientoEncargado) {
		this.lstArbolEntidadesPlaneamientoEncargado = lstArbolEntidadesPlaneamientoEncargado;
	}

	
	public DocumentosBean[] getLstDocumentos() {
		return lstDocumentos;
	}

	public void setLstDocumentos(DocumentosBean[] lstDocumentos) {
		this.lstDocumentos = lstDocumentos;
	}

	 public DeterminacionPanelBean[] getLstArbolDeterminacionesCatalogoSistematizado() {
			return lstArbolDeterminacionesCatalogoSistematizado;
		}

		public void setLstArbolDeterminacionesCatalogoSistematizado(
				DeterminacionPanelBean[] lstArbolDeterminacionesCatalogoSistematizado) {
			this.lstArbolDeterminacionesCatalogoSistematizado = lstArbolDeterminacionesCatalogoSistematizado;
		}

		public DeterminacionPanelBean[] getLstArbolDeterminacionesPlaneamientoEncargado() {
			return lstArbolDeterminacionesPlaneamientoEncargado;
		}

		public void setLstArbolDeterminacionesPlaneamientoEncargado(
				DeterminacionPanelBean[] lstArbolDeterminacionesPlaneamientoEncargado) {
			this.lstArbolDeterminacionesPlaneamientoEncargado = lstArbolDeterminacionesPlaneamientoEncargado;
		}

		public DeterminacionPanelBean[] getLstArbolDeterminacionesPlaneamientoVigente() {
			return lstArbolDeterminacionesPlaneamientoVigente;
		}

		public void setLstArbolDeterminacionesPlaneamientoVigente(
				DeterminacionPanelBean[] lstArbolDeterminacionesPlaneamientoVigente) {
			this.lstArbolDeterminacionesPlaneamientoVigente = lstArbolDeterminacionesPlaneamientoVigente;
		}
		
		public String getFecha() {
			return fecha;
		}
	
		public void setFecha(String fecha) {
			this.fecha = fecha;
		}
		
		public String getFechaConsolidacion() {
			return fechaConsolidacion;
		}
	
		public void setFechaConsolidacion(String fechaConsolidacion) {
			this.fechaConsolidacion = fechaConsolidacion;
		}
		
		public int getIdAmbito() {
			return idAmbito;
		}
	
		public void setIdAmbito(int idAmbito) {
			this.idAmbito = idAmbito;
		}
		
		public String getMunicipio() {
			return municipio;
		}
	
		public void setMunicipio(String municipio) {
			this.municipio = municipio;
		}

}
