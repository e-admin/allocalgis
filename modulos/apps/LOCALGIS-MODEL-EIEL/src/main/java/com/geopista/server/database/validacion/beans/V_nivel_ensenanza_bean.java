/**
 * V_nivel_ensenanza_bean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion.beans;


public class V_nivel_ensenanza_bean {


		     String clave="-";
		     String provincia="-";
		     String municipio="-";
		     String entidad="-";
		     String poblamient="-";
		     String orden_cent="-";
		     String nivel="-";
		     int unidades;
		     int plazas;
		     int alumnos;
		     
		public String getClave() {
			return clave;
		}
		public void setClave(String clave) {
			this.clave = clave;
		}
		public String getProvincia() {
			return provincia;
		}
		public void setProvincia(String provincia) {
			this.provincia = provincia;
		}
		public String getMunicipio() {
			return municipio;
		}
		public void setMunicipio(String municipio) {
			this.municipio = municipio;
		}
		public String getEntidad() {
			return entidad;
		}
		public void setEntidad(String entidad) {
			this.entidad = entidad;
		}
		public String getPoblamient() {
			return poblamient;
		}
		public void setPoblamient(String poblamient) {
			this.poblamient = poblamient;
		}
		public String getOrden_cent() {
			return orden_cent;
		}
		public void setOrden_cent(String orden_cent) {
			this.orden_cent = orden_cent;
		}
		public String getNivel() {
			return nivel;
		}
		public void setNivel(String nivel) {
			this.nivel = nivel;
		}
		public int getUnidades() {
			return unidades;
		}
		public void setUnidades(int unidades) {
			this.unidades = unidades;
		}
		public int getPlazas() {
			return plazas;
		}
		public void setPlazas(int plazas) {
			this.plazas = plazas;
		}
		public int getAlumnos() {
			return alumnos;
		}
		public void setAlumnos(int alumnos) {
			this.alumnos = alumnos;
		}


}
