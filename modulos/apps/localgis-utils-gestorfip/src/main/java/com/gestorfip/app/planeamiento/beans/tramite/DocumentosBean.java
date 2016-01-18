/**
 * DocumentosBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.tramite;

import java.util.Arrays;

import com.gestorfip.app.planeamiento.beans.panels.tramite.EntidadPanelBean;



public class DocumentosBean implements Comparable {
	
	private int id;
	private Long escala;
	private String archivo;
	private String nombre;
	private String codigo;
	private String comentario;
	private int grupo;
	private int tipo;
	private int tramite;
	private boolean isDirectory;
	private boolean isHoja;
	private int padre;
	private DocumentosBean[] lstHojas ;
	
	private boolean nueva = false;
	private boolean modificada = false;
	private boolean eliminada = false;
	
	private boolean isModificable = false;

	private int idLayer = -1;
	private int idFeature = -1;
	
	public boolean isModificable() {
		return isModificable;
	}

	public void setModificable(boolean isModificable) {
		this.isModificable = isModificable;
	}

	public boolean isEliminada() {
		return eliminada;
	}

	public void setEliminada(boolean eliminada) {
		this.eliminada = eliminada;
	}

	public boolean isNueva() {
		return nueva;
	}

	public void setNueva(boolean nueva) {
		this.nueva = nueva;
	}

	public boolean isModificada() {
		return modificada;
	}

	public void setModificada(boolean modificada) {
		this.modificada = modificada;
	}

	public int getTramite() {
		return tramite;
	}

	public void setTramite(int tramite) {
		this.tramite = tramite;
	}

	public int getGrupo() {
		return grupo;
	}
	
	public void setGrupo(int grupo) {
		this.grupo = grupo;
	}
	
	public int getTipo() {
		return tipo;
	}
	
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	
	public Long getEscala() {
		return escala;
	}
	
	public void setEscala(Long escala) {
		this.escala = escala;
	}
	
	public String getArchivo() {
		return archivo;
	}
	
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getComentario() {
		return comentario;
	}
	
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	
	
	public boolean isHoja() {
		return isHoja;
	}

	public void setHoja(boolean isHoja) {
		this.isHoja = isHoja;
	}

	public int getPadre() {
		return padre;
	}

	public void setPadre(int padre) {
		this.padre = padre;
	}

	public DocumentosBean[] getLstHojas() {
		return lstHojas;
	}

	public void setLstHojas(DocumentosBean[] lstHojas) {
		this.lstHojas = lstHojas;
	}

	public void addHoja(DocumentosBean hoja) {
		if(lstHojas == null){
			
			lstHojas = new DocumentosBean[1];
			lstHojas[0] = hoja;
		
		}
		else{
			lstHojas = (DocumentosBean[]) Arrays.copyOf(lstHojas, 
					lstHojas.length+1);
		
			lstHojas[lstHojas.length-1] = hoja;
		}
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


	public int compareTo(Object o) {
		DocumentosBean db = (DocumentosBean) o;
		
		return nombre.compareToIgnoreCase(db.getNombre());
	}

}
