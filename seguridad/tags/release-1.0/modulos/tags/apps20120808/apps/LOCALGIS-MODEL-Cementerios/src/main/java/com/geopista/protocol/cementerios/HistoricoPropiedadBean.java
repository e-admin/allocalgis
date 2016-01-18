package com.geopista.protocol.cementerios;

import java.io.Serializable;
import java.util.Date;



public class HistoricoPropiedadBean extends ElemFeatureBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id_historico;
	private int id_elem;
	private int tipo;
	private String tipoStr;
	private Date fechaOperacion;
	private String comentario;
	
	Object elem;

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
