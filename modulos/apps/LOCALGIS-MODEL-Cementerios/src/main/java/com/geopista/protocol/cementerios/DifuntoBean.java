/**
 * DifuntoBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.cementerios;

import java.io.Serializable;
import java.util.Date;


/**
 * Clase que implementa un objeto de tipo Difunto
 */
public class DifuntoBean extends ElemCementerioBean implements Serializable{

	private Date fecha_defuncion;
	private int edad;
	private int grupo;
	private ServicioBean servicio;
	private PersonaBean persona;
	private DatosFallecimientoBean datosFallecimiento;
	private int id_plaza;
	private int id_difunto;
	private String codigo;

	/**
	 * Grupo al que pertenece el cadavar.. puede ser 1 0 2s
	 * @return
	 */
	public int getGrupo() {
		return grupo;
	}
	public void setGrupo(int grupo) {
		this.grupo = grupo;
	}

	public Date getFecha_defuncion() {
		return fecha_defuncion;
	}
	public void setFecha_defuncion(Date fecha_defuncion) {
		this.fecha_defuncion = fecha_defuncion;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public ServicioBean getServicio() {
		return servicio;
	}
	public void setServicio(ServicioBean servicio) {
		this.servicio = servicio;
	}
	public PersonaBean getPersona() {
		return persona;
	}
	public void setPersona(PersonaBean persona) {
		this.persona = persona;
	}
	public DatosFallecimientoBean getDatosFallecimiento() {
		return datosFallecimiento;
	}
	public void setDatosFallecimiento(DatosFallecimientoBean datosFallecimiento) {
		this.datosFallecimiento = datosFallecimiento;
	}
	public int getId_plaza() {
		return id_plaza;
	}
	public void setId_plaza(int id_plaza) {
		this.id_plaza = id_plaza;
	}
	public int getId_difunto() {
		return id_difunto;
	}
	public void setId_difunto(int id_difunto) {
		this.id_difunto = id_difunto;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
}
