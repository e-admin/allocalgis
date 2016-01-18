/**
 * CondicionUrbanisticaCasoRegimenesBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.tramite;

public class CondicionUrbanisticaCasoRegimenesBean {
	
	  private int id;
	  private String comentario; 
	  private String valor;
	  private int superposicion;
	  private int valorreferencia_determinacionid;
	  private int determinacionregimen_determinacionid;
	  private int casoaplicacion_casoid;
	  private int caso;
	  
	  
	  // se utiliza para la gestion en pantall
	  	private CondicionUrbanisticaCasoRegimenRegimenesBean cucrr;
		  
		public CondicionUrbanisticaCasoRegimenRegimenesBean getCucrr() {
			return cucrr;
		}
	
		public void setCucrr(CondicionUrbanisticaCasoRegimenRegimenesBean cucrr) {
			this.cucrr = cucrr;
		}
	////

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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
	
	public int getSuperposicion() {
		return superposicion;
	}
	
	public void setSuperposicion(int superposicion) {
		this.superposicion = superposicion;
	}
	
	public int getValorreferencia_determinacionid() {
		return valorreferencia_determinacionid;
	}
	
	public void setValorreferencia_determinacionid(
			int valorreferenciaDeterminacionid) {
		valorreferencia_determinacionid = valorreferenciaDeterminacionid;
	}
	
	public int getDeterminacionregimen_determinacionid() {
		return determinacionregimen_determinacionid;
	}
	
	public void setDeterminacionregimen_determinacionid(
			int determinacionregimenDeterminacionid) {
		determinacionregimen_determinacionid = determinacionregimenDeterminacionid;
	}
	
	public int getCasoaplicacion_casoid() {
		return casoaplicacion_casoid;
	}
	
	public void setCasoaplicacion_casoid(int casoaplicacionCasoid) {
		casoaplicacion_casoid = casoaplicacionCasoid;
	}
	
	public int getCaso() {
		return caso;
	}
	
	public void setCaso(int caso) {
		this.caso = caso;
	} 
	
	  /////////////////////
	  // SE utiliza para la gestion en pantalla de la lista de regimenes en la asociacion
	private String nombre;
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	////////////////////////

}
