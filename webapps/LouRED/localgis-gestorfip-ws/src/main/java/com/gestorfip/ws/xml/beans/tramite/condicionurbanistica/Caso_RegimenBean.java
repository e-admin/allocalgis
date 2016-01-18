package com.gestorfip.ws.xml.beans.tramite.condicionurbanistica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Caso_RegimenBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4146653691599357879L;

	private String comentario;
	private String valor;
	private String superposicion;
	private String valorreferencia_determinacion;
	private String determinacionregimen_determinacion;
	private String casoaplicacion_caso;
	
	private List<Caso_Regimen_RegimenEspecificoBean> regimenesespecificos = new ArrayList<Caso_Regimen_RegimenEspecificoBean>();
	private int seq_id;
	
	public Caso_RegimenBean() {
		// TODO Auto-generated constructor stub
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getSuperposicion() {
		return superposicion;
	}

	public void setSuperposicion(String superposicion) {
		this.superposicion = superposicion;
	}

	public String getValorreferencia_determinacion() {
		return valorreferencia_determinacion;
	}

	public void setValorreferencia_determinacion(String valorreferenciaDeterminacion) {
		valorreferencia_determinacion = valorreferenciaDeterminacion;
	}

	public String getDeterminacionregimen_determinacion() {
		return determinacionregimen_determinacion;
	}

	public void setDeterminacionregimen_determinacion(
			String determinacionregimenDeterminacion) {
		determinacionregimen_determinacion = determinacionregimenDeterminacion;
	}

	public String getCasoaplicacion_caso() {
		return casoaplicacion_caso;
	}

	public void setCasoaplicacion_caso(String casoaplicacionCaso) {
		casoaplicacion_caso = casoaplicacionCaso;
	}

	public List<Caso_Regimen_RegimenEspecificoBean> getRegimenesespecificos() {
		return regimenesespecificos;
	}

	public void setRegimenesespecificos(
			List<Caso_Regimen_RegimenEspecificoBean> regimenesespecificos) {
		this.regimenesespecificos = regimenesespecificos;
	}

	public int getSeq_id() {
		return seq_id;
	}

	public void setSeq_id(int seqId) {
		seq_id = seqId;
	}

}
