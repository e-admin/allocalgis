package com.geopista.ui.plugin.georreferenciacionExterna;

import java.io.Serializable;

public class FiltroData implements Serializable {
	
	public static final String NUMERICO_MAYOR = ">";
	public static final String NUMERICO_MENOR = "<";
	public static final String NUMERICO_MAYOR_IGUAL = ">=";
	public static final String NUMERICO_MENOR_IGUAL = "<=";
	public static final String NUMERICO_IGUAL = "=";
	public static final String NUMERICO_COINCIDIR = "IN";
	
	public static final String TEXTO_IGUAL = "=";
	public static final String TEXTO_PATRON = "MATCHES/LIKE";
	public static final String TEXTO_COINCIDIR = "IN";
	
	
	private String valor;
	private String operador;
	private String campo;
	private String tipoCampo;
	
	
	public String getTipoCampo() {
		return tipoCampo;
	}
	
	public void setTipoCampo(String tipoCampo) {
		this.tipoCampo = tipoCampo;
	}
	
	public String getCampo() {
		return campo;
	}
	
	public void setCampo(String campo) {
		this.campo = campo;
	}
	
	public String getValor() {
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public String getOperador() {
		return operador;
	}
	
	public void setOperador(String operador) {
		this.operador = operador;
	}
}
