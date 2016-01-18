/**
 * FiltroData.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
