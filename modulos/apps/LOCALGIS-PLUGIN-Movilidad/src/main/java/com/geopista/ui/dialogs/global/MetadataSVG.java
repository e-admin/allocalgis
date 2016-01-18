/**
 * MetadataSVG.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.global;

public class MetadataSVG{

	//datos recogidos del SVG
	protected String encabezado;
	protected String grupo;
	protected String path;
	protected String numCelda;
	protected String nombreMetadato;

	public MetadataSVG(String encabezado, String grupo, String path, String numCelda, String nombreMetadato) {
		super();
		this.encabezado = encabezado;
		this.grupo = grupo;
		this.path = path;
		this.numCelda = numCelda;		
		this.nombreMetadato = nombreMetadato;		
	}

	public String getEncabezado() {
		return encabezado;
	}

	public void setEncabezado(String encabezado) {
		this.encabezado = encabezado;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNumCelda() {
		return numCelda;
	}

	public void setNumCelda(String numCelda) {
		this.numCelda = numCelda;
	}

	public String getNombreMetadato() {
		return nombreMetadato;
	}

	public void setNombreMetadato(String nombreMetadato) {
		this.nombreMetadato = nombreMetadato;
	}
	
}
