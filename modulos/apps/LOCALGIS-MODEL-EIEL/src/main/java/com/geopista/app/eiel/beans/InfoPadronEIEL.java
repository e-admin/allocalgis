/**
 * InfoPadronEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class InfoPadronEIEL extends WorkflowEIEL implements Serializable, EIELPanel{
	
	

	public InfoPadronEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
	}

	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
			
		return fields;
	}
	
	String C;
	String P;
	String ES;
	String ND;
	String Oficial;
	String Tradicional;
	String Categoria;
	float TotalPop;
	float Hombres;
	float Mujeres;
	float TotalViv;
	float Principales;
	float Noprincipales;
	String Comentario;
	String CON3;
	public String getC() {
		return C;
	}

	public void setC(String c) {
		C = c;
	}

	public String getP() {
		return P;
	}

	public void setP(String p) {
		P = p;
	}

	public String getES() {
		return ES;
	}

	public void setES(String eS) {
		ES = eS;
	}

	public String getND() {
		return ND;
	}

	public void setND(String nD) {
		ND = nD;
	}

	public String getOficial() {
		return Oficial;
	}

	public void setOficial(String oficial) {
		Oficial = oficial;
	}

	public String getTradicional() {
		return Tradicional;
	}

	public void setTradicional(String tradicional) {
		Tradicional = tradicional;
	}

	public String getCategoria() {
		return Categoria;
	}

	public void setCategoria(String categoria) {
		Categoria = categoria;
	}

	public float getTotalPop() {
		return TotalPop;
	}

	public void setTotalPop(float totalPop) {
		TotalPop = totalPop;
	}

	public float getHombres() {
		return Hombres;
	}

	public void setHombres(float hombres) {
		Hombres = hombres;
	}

	public float getMujeres() {
		return Mujeres;
	}

	public void setMujeres(float mujeres) {
		Mujeres = mujeres;
	}

	public float getTotalViv() {
		return TotalViv;
	}

	public void setTotalViv(float totalViv) {
		TotalViv = totalViv;
	}

	public float getPrincipales() {
		return Principales;
	}

	public void setPrincipales(float principales) {
		Principales = principales;
	}

	public float getNoprincipales() {
		return Noprincipales;
	}

	public void setNoprincipales(float noprincipales) {
		Noprincipales = noprincipales;
	}

	public String getComentario() {
		return Comentario;
	}

	public void setComentario(String comentario) {
		Comentario = comentario;
	}

	public String getCON3() {
		return CON3;
	}

	public void setCON3(String cON3) {
		CON3 = cON3;
	}
	
	@Override
	public String toString() {
		return "InfoPadronEIEL [C=" + C + ", P=" + P + ", ES=" + ES + ", ND="
				+ ND + ", Oficial=" + Oficial + "]";
	}	
	
	
}
