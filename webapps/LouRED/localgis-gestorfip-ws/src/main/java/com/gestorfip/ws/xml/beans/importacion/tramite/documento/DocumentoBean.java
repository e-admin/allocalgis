package com.gestorfip.ws.xml.beans.importacion.tramite.documento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DocumentoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5209609503835975073L;

	private String grupo;
	private String tipo;
	private String escala;
	private String archivo;
	private String nombre;
	private String codigo;
	private String comentario;
	private List<HojaBean> hojas = new ArrayList<HojaBean>();
	
	public DocumentoBean() {
		// TODO Auto-generated constructor stub
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEscala() {
		return escala;
	}

	public void setEscala(String escala) {
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

	public List<HojaBean> getHojas() {
		return hojas;
	}

	public void setHojas(List<HojaBean> hojas) {
		this.hojas = hojas;
	}

}
