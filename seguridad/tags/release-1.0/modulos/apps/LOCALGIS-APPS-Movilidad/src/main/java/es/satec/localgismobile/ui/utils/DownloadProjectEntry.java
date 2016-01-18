package es.satec.localgismobile.ui.utils;

import java.text.SimpleDateFormat;

/**Se almacenan los datos del proyecto
 * que se reciben del servidor**/
public class DownloadProjectEntry implements Comparable {
	private String idProyecto;
	private String nombreProyecto;
	private String fechaExtraccion;
	private String idMunicipio;
	private String idMap;
	public String getFechaExtraccion() {
		return fechaExtraccion;
	}
	public void setFechaExtraccion(String fechaExtraccion) {
		this.fechaExtraccion = fechaExtraccion;
	}
	public String getIdMap() {
		return idMap;
	}
	public void setIdMap(String idMap) {
		this.idMap = idMap;
	}
	public String getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(String idMunicipio) {
		this.idMunicipio = idMunicipio;
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
	public int compareTo(Object o) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try{ 
			return -(sdf.parse(this.getFechaExtraccion()).compareTo(sdf.parse(((DownloadProjectEntry) o).getFechaExtraccion())));
		}catch (Exception e){
			return 0;
		}
	}
}
