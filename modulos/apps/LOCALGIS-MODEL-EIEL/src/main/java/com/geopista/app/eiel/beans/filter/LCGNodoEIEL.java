/**
 * LCGNodoEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans.filter;

import java.io.Serializable;

public class LCGNodoEIEL implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2398878193852911999L;
	private String clave;
	private String nodo;
	private String tagTraduccion;
	private String traduccion;
	private String tabla;
	private String nombreFiltro;
	private String bean;
	private String categoria;
	private String layer;
	
	private int elementosTemporales=-1;
	private int elementosPublicables=-1;
	private int elementosExternos=-1;
	private int elementosBorrables=-1;
	private boolean conectividad;
	

	

	public LCGNodoEIEL(String clave, String nodo, String tagTraduccion,
			String traduccion, String tabla, String nombreFiltro, String bean) {
		super();
		this.clave = clave;
		this.nodo = nodo;
		this.tagTraduccion = tagTraduccion;
		this.traduccion = traduccion;
		this.tabla = tabla;
		this.nombreFiltro = nombreFiltro;
		this.bean = bean;
	}

	public LCGNodoEIEL() {
		// TODO Auto-generated constructor stub
	}

	public String getNodo() {
		return nodo;
	}
	public void setNodo(String nodo) {
		this.nodo = nodo;
	}
	public String getTabla() {
		return tabla;
	}
	public void setTabla(String tabla) {
		this.tabla = tabla;
	}
	public String getTraduccion() {
		return traduccion;
	}
	public void setTraduccion(String traduccion) {
		this.traduccion = traduccion;
	}

	public String getTagTraduccion() {
		return tagTraduccion;
	}

	public void setTagTraduccion(String tagTraduccion) {
		this.tagTraduccion = tagTraduccion;
	}

	public String getNombreFiltro() {
		return nombreFiltro;
	}

	public void setNombreFiltro(String nombreFiltro) {
		this.nombreFiltro = nombreFiltro;
	}

	public String getBean() {
		return bean;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}

	@Override
	public String toString() {
		return "NodoEIEL [nodo=" + nodo + ", tagTraduccion=" + tagTraduccion
				+ ", traduccion=" + traduccion + ", tabla=" + tabla
				+ ", nombreFiltro=" + nombreFiltro + ", bean=" + bean + "]";
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public int getElementosTemporales() {
		return elementosTemporales;
	}

	public void setElementosTemporales(int elementosTemporales) {
		this.elementosTemporales = elementosTemporales;
	}

	public int getElementosPublicables() {
		return elementosPublicables;
	}

	public void setElementosPublicables(int elementosPublicables) {
		this.elementosPublicables = elementosPublicables;
	}
	
	public int getElementosBorrables() {
		return elementosBorrables;
	}

	public void setElementosBorrables(int elementosBorrables) {
		this.elementosBorrables = elementosBorrables;
	}
	
	public int getElementosExternos() {
		return elementosExternos;
	}

	public void setElementosExternos(int elementosExternos) {
		this.elementosExternos= elementosExternos;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public boolean isConectividad() {
		return conectividad;
	}

	public void setConectividad(boolean conectividad) {
		this.conectividad = conectividad;
	}



	

}
