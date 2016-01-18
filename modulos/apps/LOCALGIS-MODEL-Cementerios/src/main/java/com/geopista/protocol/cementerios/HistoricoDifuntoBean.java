/**
 * HistoricoDifuntoBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.cementerios;

import java.io.Serializable;
import java.util.Date;



public class HistoricoDifuntoBean extends ElemFeatureBean implements Serializable{
	
	private int id_historico;
	private int id_elem;
	private int tipo;
	private String tipoStr;
	private Date fechaOperacion;
	private String comentario;
	
	private DifuntoBean difunto;
	private Object elem;

	/**Métodos Getter y Setter**/
	
	public int getId_historico() {
		return id_historico;
	}

	public void setId_historico(int id_historico) {
		this.id_historico = id_historico;
	}

	public int getId_elem() {
		return id_elem;
	}

	public void setId_elem(int id_elem) {
		this.id_elem = id_elem;
	}


	public Date getFechaOperacion() {
		return fechaOperacion;
	}

	public void setFechaOperacion(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public DifuntoBean getDifunto() {
		return difunto;
	}

	public void setDifunto(DifuntoBean difunto) {
		this.difunto = difunto;
	}

	public Object getElem() {
		return elem;
	}

	public void setElem(Object elem) {
		this.elem = elem;
	}

	public String getTipoStr() {
		return tipoStr;
	}

	public void setTipoStr(String tipoStr) {
		this.tipoStr = tipoStr;
	}
	

}
