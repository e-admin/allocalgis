/**
 * ExtractionProject.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Almacena los datos sacados de BBDD sobre los proyectos de extracción
 * @author irodriguez
 *
 */
public class ExtractionProject {

	private String idProyecto;
	private String nombreProyecto; 
	private Date fechaExtraccion;
	private int idEntidad;
	private String idMap;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public ExtractionProject() {
		super();
	}

	public ExtractionProject(String idProyecto, String nombre_proyecto,
			Date fechaExtraccion, int idEntidad, String idMap) {
		super();
		this.idProyecto = idProyecto;
		this.nombreProyecto = nombre_proyecto;
		this.fechaExtraccion = fechaExtraccion;
		this.idEntidad = idEntidad;
		this.idMap = idMap;
	}

	public String getIdProyecto() {
		return idProyecto;
	}

	public void setIdProyecto(String idProyecto) {
		this.idProyecto = idProyecto;
	}

	public String getNombreProyecto() {
		return nombreProyecto;
	}

	public void setNombreProyecto(String nombreProyecto) {
		this.nombreProyecto = nombreProyecto;
	}

	public Date getFechaExtraccion() {
		return fechaExtraccion;
	}

	public void setFechaExtraccion(Date fechaExtraccion) {
		this.fechaExtraccion = fechaExtraccion;
	}

	public int getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(int idEntidad) {
		this.idEntidad = idEntidad;
	}

	public String getIdMap() {
		return idMap;
	}

	public void setIdMap(String idMap) {
		this.idMap = idMap;
	}
	
	/**
	 * Devuelve el bean en formato XML para ser interpretado en un dispositivo móvil
	 * @return
	 */
	public String toXMLFormat(){
		StringBuffer result = new StringBuffer();		
		result.append("<project");
		result.append(" idProyecto=\""+idProyecto+"\"");
		result.append(" nombreProyecto=\""+nombreProyecto+"\"");
		result.append(" fechaExtraccion=\""+sdf.format(fechaExtraccion)+"\"");
		result.append(" idMunicipio=\""+idEntidad+"\"");
		result.append(" idMap=\""+idMap+"\"");
		result.append(" />");
		return result.toString();
	}
	
}
