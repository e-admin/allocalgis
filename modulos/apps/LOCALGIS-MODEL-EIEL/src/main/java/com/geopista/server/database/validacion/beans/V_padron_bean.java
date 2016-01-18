/**
 * V_padron_bean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion.beans;


public class V_padron_bean {
	
	
		  String codprov="-";
		  String codmunic="-";
		  int n_hombre_a1;
		  int n_mujeres_a1;
		  int total_poblacion_a1;
		
		public String getCodprov() {
			return codprov;
		}
		public void setCodprov(String codprov) {
			this.codprov = codprov;
		}
		public String getCodmunic() {
			return codmunic;
		}
		public void setCodmunic(String codmunic) {
			this.codmunic = codmunic;
		}
		public int getN_hombre_a1() {
			return n_hombre_a1;
		}
		public void setN_hombre_a1(int nHombreA1) {
			n_hombre_a1 = nHombreA1;
		}
		public int getN_mujeres_a1() {
			return n_mujeres_a1;
		}
		public void setN_mujeres_a1(int nMujeresA1) {
			n_mujeres_a1 = nMujeresA1;
		}
		public int getTotal_poblacion_a1() {
			return total_poblacion_a1;
		}
		public void setTotal_poblacion_a1(int totalPoblacionA1) {
			total_poblacion_a1 = totalPoblacionA1;
		}		 
		
	
}
